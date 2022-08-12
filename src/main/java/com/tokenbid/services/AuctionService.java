package com.tokenbid.services;

import java.util.List;
import java.util.Optional;

import com.tokenbid.models.Bid;
import com.tokenbid.models.Item;
import com.tokenbid.models.User;
import com.tokenbid.repositories.BidRepository;
import com.tokenbid.repositories.ItemRepository;
import com.tokenbid.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tokenbid.models.Auction;
import com.tokenbid.repositories.AuctionRepository;

@Service
public class AuctionService implements IService<Auction> {
    private AuctionRepository auctionRepository;
    private EmailService emailService;
    private ItemRepository itemRepository;
    private BidRepository bidRepository;
    private UserRepository userRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository, EmailService emailService, ItemRepository itemRepository, BidRepository bidRepository, UserRepository userRepository) {
        this.auctionRepository = auctionRepository;
        this.emailService = emailService;
        this.itemRepository =itemRepository;
        this.bidRepository = bidRepository;
        this. userRepository = userRepository;
    }

    @Override
    public int add(Auction item) {
        return auctionRepository.save(item).getAuctionId();
    }

    @Override
    public void update(Auction item) {
        if (auctionRepository.findById(item.getAuctionId()).isPresent()) {
            auctionRepository.save(item);
        }
    }

    @Override
    public void delete(int id) {
        if (auctionRepository.findById(id).isPresent()) {
            Auction auction = auctionRepository.findById(id).get();
            Item item = itemRepository.findById(auction.getItemId()).get();
            Bid bid = bidRepository.findById(auction.getCurrentBid()).get();
            User seller = userRepository.findById(item.getUserId()).get();
            User buyer = userRepository.findById(bid.getUserId()).get();
            emailService.sendEmail(seller.getEmail(), "Notification","Congratulations, your item " +item.getTitle() +" was successfully sold with " + auction.getCurrentBid() + "tokens");
            emailService.sendEmail(buyer.getEmail(), "Notification","Congratulations, your bought " +item.getTitle() +" with " + auction.getCurrentBid() + "tokens");
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

    public void testMessage() {
        System.out.println("AuctionService.testMessage()");
    }
}
