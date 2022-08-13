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
    public int add(Auction item) {
        return auctionRepository.save(item).getAuctionId();
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
                    // TODO : Email seller when they cancel an auction?
                    break;
            }
        }
    }

    @Override
    public void delete(int id) {
        if (auctionRepository.findById(id).isPresent()) {
            auctionRepository.deleteById(id);
        }
    }

    @Override
    public Auction getById(int id) {
        if (auctionRepository.findById(id).isPresent()) {
            return auctionRepository.findById(id).get();
        }

        return null;
    }

    @Override
    public List<Auction> getAll() {
        return auctionRepository.findAll();
    }

    private void notifySold(User seller, User buyer, Item item, Bid winningBid) {
        emailService.sendEmail(seller.getEmail(), "Notification","Congratulations, your item " + item.getTitle() + " was successfully sold for " + winningBid.getBid() + " tokens");
        emailService.sendEmail(buyer.getEmail(), "Notification","Congratulations, you won the bid for " + item.getTitle() + " at " + winningBid.getBid() + " tokens");
    }
    private void notifyNotSold(User seller, Item item) {
        emailService.sendEmail(seller.getEmail(), "Notification", "Hi " + seller.getFirstName() + " " + seller.getLastName() + ",\nYour auction for item " + item.getTitle() + " has ended with no successful bids.");
    }

}
