package com.tokenbid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tokenbid.models.Bid;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {
    /**
     * To get maximum bid amount and corresponding User ID as per Auction
     * @param auctionId
     * @return Bid
     */
    @Query(value = "SELECT * FROM bids WHERE auction_id =:auctionId AND bid = (SELECT MAX(bid) FROM bids)", nativeQuery = true)
    public Bid getHighestBidForAnAuction(@Param("auctionId") int auctionId);
}
