package com.tokenbid.services;

import java.util.List;

import com.tokenbid.controllers.AuctionController;
import com.tokenbid.models.Bid;
import com.tokenbid.models.Item;
import com.tokenbid.models.User;
import com.tokenbid.utils.EmailUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tokenbid.models.Auction;
import com.tokenbid.repositories.AuctionRepository;

@Service
public class AuctionService implements IService<Auction> {
    private AuctionRepository auctionRepository;
    private EmailUtil emailUtil;
    private ItemService itemService;
    private BidService bidService;
    private UserService userService;
    private static Logger logger = LogManager.getLogger(AuctionController.class.getName());

    @Autowired
    public AuctionService(AuctionRepository auctionRepository, EmailUtil emailUtil, ItemService itemService,
            BidService bidService, UserService userService) {
        this.auctionRepository = auctionRepository;
        this.emailUtil = emailUtil;
        this.itemService = itemService;
        this.bidService = bidService;
        this.userService = userService;
    }

    @Override
    public int add(Auction auction) {
        return auctionRepository.save(auction).getAuctionId();
    }

    @Override
    public void update(Auction auction) {
        logger.debug("Updating auction with auction ID: " + auction.getAuctionId());
        if (auctionRepository.findById(auction.getAuctionId()).isPresent()) {
            auctionRepository.save(auction);
            Item item = itemService.getById(auction.getItemId());
            User seller = userService.getById(item.getUserId());
            System.out.println("Auction status: " + auction.getStatus());
            switch (auction.getStatus()) {
                case "Sold":
                    Bid winningBid = bidService.getHighestBidForAnAuction(auction.getAuctionId());
                    User buyer = userService.getById(winningBid.getUserId());
                    notifySold(seller, buyer, item, winningBid);
                    processSale(item, winningBid, buyer, seller);
                    break;
                case "Not Sold":
                    notifyNotSold(seller, item);
                    break;
                case "Cancelled":
                    notifyCancelled(seller, item);
                    break;
            }
        }
    }

    /**
     * Transfer item ownership from seller to buyer, add tokens from winning bid to
     * the seller (tokens have already been subtracted from buyer)
     * Delete all bids belonging to this auction
     * 
     * @param item   Item being sold
     * @param bid    Winning bid
     * @param buyer  User buying the item
     * @param seller User selling the item
     */
    protected void processSale(Item item, Bid bid, User buyer, User seller) {
        logger.debug("Transferring the ownership");
        item.setUserId(buyer.getUserId());
        itemService.update(item);

        logger.debug("tokens added to seller");
        seller.setTokens(seller.getTokens() + bid.getBid());

        logger.debug("delete all bids for this auction");
        bidService.deleteForAuction(bid.getAuctionId());
    }

    @Override
    public void delete(int auctionId) {
        if (auctionRepository.findById(auctionId).isPresent()) {
            auctionRepository.deleteById(auctionId);
        }
    }

    @Override
    public Auction getById(int auctionId) {
        if (auctionRepository.findById(auctionId).isPresent()) {
            return auctionRepository.findById(auctionId).get();
        }
        return null;
    }

    @Override
    public List<Auction> getAll() {
        return auctionRepository.findAll();
    }

    /**
     * @return A list of auctions with an end time less than 1 hour from the current
     *         time
     */
    public List<Auction> getAuctionsEndingInHalfAnHour() {
        return auctionRepository.findAuctionsEndingInNextHalfAnHour();
    }

    /**
     * @return A list of auctions with a status of 'In Progress' and an end time in
     *         the future
     */
    public List<Auction> getActive() {
        logger.debug("Getting active auctions");
        return auctionRepository.findAllActiveAuctions();
    }

    /**
     * Compose and send emails to the seller and buyer when an auction ends with a
     * sale
     * 
     * @param seller     User selling the item
     * @param buyer      User who won the auction
     * @param item       Item being sold
     * @param winningBid The auction's winning bid
     */
    protected void notifySold(User seller, User buyer, Item item, Bid winningBid) {
        String soldBody = "<div style=\"background-color:#ad8; min-height: 300px\">" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">"
                +
                "<p style=\"margin: 0; padding: 0; text-align: center\">Item Sold</p>" +
                "</div>" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">"
                +
                "<p style=\"margin-top: 1.5em\">Hi " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
                "<p style=\"margin-top: 1.5em\">Congratulations! Your item '" + item.getTitle() + "' was sold for "
                + winningBid.getBid() + " tokens to " + buyer.getFirstName() + " " + buyer.getLastName() + ".</p>" +
                "</div>" +
                "</div>";
        String boughtBody = "<div style=\"background-color:#ad8; min-height: 300px\">" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">"
                +
                "<p style=\"margin: 0; padding: 0; text-align: center\">Auction Won</p>" +
                "</div>" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">"
                +
                "<p style=\"margin-top: 1.5em\">Hi " + buyer.getFirstName() + " " + buyer.getLastName() + ",</p>" +
                "<p style=\"margin-top: 1.5em\">Congratulations! You won the bid for '" + item.getTitle() + "' at "
                + winningBid.getBid() + " tokens.</p>" +
                "</div>" +
                "</div>";
        emailUtil.sendHTMLEmail(seller.getEmail(), "Auction Ended", soldBody);
        emailUtil.sendHTMLEmail(buyer.getEmail(), "Auction Ended", boughtBody);
    }

    /**
     * Compose and send an email to the seller when an auction ends with no sale
     * 
     * @param seller User selling the item
     * @param item   Item for auction that was not sold
     */
    protected void notifyNotSold(User seller, Item item) {
        String notSoldBody = "<div style=\"background-color:#abc; min-height: 300px\">" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">"
                +
                "<p style=\"margin: 0; padding: 0; text-align: center\">Item Not Sold</p>" +
                "</div>" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">"
                +
                "<p style=\"margin-top: 1.5em\">Hi " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
                "<p style=\"margin-top: 1.5em\">Your auction for item '" + item.getTitle()
                + "' has ended with no successful bids.</p>" +
                "</div>" +
                "</div>";
        emailUtil.sendHTMLEmail(seller.getEmail(), "Auction Ended", notSoldBody);
    }

    /**
     * Compose and send an email to the seller when they cancel their auction
     * 
     * @param seller User selling the item
     * @param item   Item for auction
     */
    protected void notifyCancelled(User seller, Item item) {
        String cancelledBody = "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">"
                +
                "<p style=\"margin: 0; padding: 0; text-align: center\">Cancelled</p>" +
                "</div>" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">"
                +
                "<p style=\"margin-top: 1.5em\">Hi " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
                "<p style=\"margin-top: 1.5em\">Your auction for item '" + item.getTitle()
                + "' was successful cancelled.</p>" +
                "</div>";
        emailUtil.sendHTMLEmail(seller.getEmail(), "Auction Ended", cancelledBody);
    }
}
