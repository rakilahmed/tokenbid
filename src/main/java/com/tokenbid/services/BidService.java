package com.tokenbid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tokenbid.models.Bid;
import com.tokenbid.repositories.BidRepository;

@Service
public class BidService implements IService<Bid> {
    private BidRepository bidRepository;

    @Autowired
    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    @Override
    public int add(Bid item) {
        return bidRepository.save(item).getBidId();
    }

    @Override
    public void update(Bid item) {
        if (bidRepository.findById(item.getBidId()).isPresent()) {
            bidRepository.save(item);
        }
    }

    @Override
    public void delete(int id) {
        if (bidRepository.findById(id).isPresent()) {
            bidRepository.deleteById(id);
        }
    }

    @Override
    public Bid getById(int id) {
        if (bidRepository.findById(id).isPresent()) {
            return bidRepository.findById(id).get();
        }

        return null;
    }

    @Override
    public List<Bid> getAll() {
        return bidRepository.findAll();
    }

    /**
     * Find the highest Bid and associated user From Bid Table, Input auction ID output the highest bid and user id
     */
    public Bid getHighestBidForAnAuction(int auctionID){
        System.out.println("Highest Bid :" +bidRepository.getHighestBidForAnAuction(auctionID).getBid() + "User ID :" +bidRepository.getHighestBidForAnAuction(auctionID).getUserId());
        return bidRepository.getHighestBidForAnAuction(auctionID);
    }

}
