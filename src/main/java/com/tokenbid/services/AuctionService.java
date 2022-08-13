package com.tokenbid.services;

import java.util.List;

import com.tokenbid.models.Bid;
import com.tokenbid.models.Item;
import com.tokenbid.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tokenbid.models.Auction;
import com.tokenbid.repositories.AuctionRepository;

@Service
public class AuctionService implements IService<Auction> {
    private AuctionRepository auctionRepository;
    private EmailService emailService;
    private ItemService itemService;
    private BidService bidService;
    private UserService userService;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository, EmailService emailService, ItemService itemService, BidService bidService, UserService userService) {
        this.auctionRepository = auctionRepository;
        this.emailService = emailService;
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
        if (auctionRepository.findById(auction.getAuctionId()).isPresent()) {
            auctionRepository.save(auction);
            Item item = itemService.getById(auction.getItemId());
            User seller = userService.getById(item.getUserId());
            switch (auction.getStatus()) {
                case "Sold":
                    Bid winningBid = bidService.getHighestBidForAnAuction(auction.getAuctionId());
                    User buyer = userService.getById(winningBid.getUserId());
                    notifySold(seller, buyer, item, winningBid);
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
     * Compose and send emails to the seller and buyer when an auction ends with a sale
     * @param seller User selling the item
     * @param buyer User who won the auction
     * @param item Item being sold
     * @param winningBid The auction's winning bid
     */
    private void notifySold(User seller, User buyer, Item item, Bid winningBid) {
        String soldBody =
                "<div style=\"background-color:#ad8; min-height: 300px\">" +
                    "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">" +
                        "<p style=\"margin: 0; padding: 0; text-align: center\">Item Sold</p>" +
                    "</div>" +
                    "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">" +
                        "<p style=\"margin-top: 1.5em\">Hi " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
                        "<p style=\"margin-top: 1.5em\">Congratulations! Your item '" + item.getTitle() + "' was sold for " + winningBid.getBid() + " tokens to " + buyer.getFirstName() + " " + buyer.getLastName() + ".</p>" +
                    "</div>" +
                "</div>";
        String boughtBody =
                "<div style=\"background-color:#ad8; min-height: 300px\">" +
                    "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">" +
                        "<p style=\"margin: 0; padding: 0; text-align: center\">Auction Won</p>" +
                    "</div>" +
                    "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">" +
                        "<p style=\"margin-top: 1.5em\">Hi " + buyer.getFirstName() + " " + buyer.getLastName() + ",</p>" +
                        "<p style=\"margin-top: 1.5em\">Congratulations! You won the bid for '" + item.getTitle() + "' at " + winningBid.getBid() + " tokens.</p>" +
                    "</div>" +
                "</div>";
        emailService.sendHTMLEmail(seller.getEmail(), "Auction Ended", soldBody);
        emailService.sendHTMLEmail(buyer.getEmail(), "Auction Ended", boughtBody);
    }

    /**
     * Compose and send an email to the seller when an auction ends with no sale
     * @param seller User selling the item
     * @param item Item for auction that was not sold
     */
    private void notifyNotSold(User seller, Item item) {
        String notSoldBody =
                "<div style=\"background-color:#abc; min-height: 300px\">" +
                    "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">" +
                        "<p style=\"margin: 0; padding: 0; text-align: center\">Item Not Sold</p>" +
                    "</div>" +
                    "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">" +
                        "<p style=\"margin-top: 1.5em\">Hi " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
                        "<p style=\"margin-top: 1.5em\">Your auction for item '" + item.getTitle() + "' has ended with no successful bids.</p>" +
                    "</div>" +
                "</div>";
        emailService.sendHTMLEmail(seller.getEmail(), "Auction Ended", notSoldBody);
    }

    /**
     * Compose and send an email to the seller when they cancel their auction
     * @param seller User selling the item
     * @param item Item for auction
     */
    private void notifyCancelled(User seller, Item item) {
        String cancelledBody =
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">" +
                    "<p style=\"margin: 0; padding: 0; text-align: center\">Cancelled</p>" +
                "</div>" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">" +
                    "<p style=\"margin-top: 1.5em\">Hi " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
                    "<p style=\"margin-top: 1.5em\">Your auction for item '" + item.getTitle() + "' was successful cancelled.</p>" +
                "</div>";
        emailService.sendHTMLEmail(seller.getEmail(), "Auction Ended", cancelledBody);
    }
}
