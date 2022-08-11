package com.tokenbid.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bids")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bidId;

    @Column(name = "auction_id")
    private int auctionId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "bid")
    private int bid;

    @Column(name = "is_outbid")
    @JsonProperty("isOutbid")
    private boolean isOutbid = false;

    /**
     * Parameterized constructor for Bid class.
     * 
     * @param auctionId The auction id of the bid.
     * @param userId    The user id of the bid.
     * @param bid       The bid of the bid.
     */
    public Bid(int auctionId, int userId, int bid) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.bid = bid;
    }

    /**
     * Parameterized constructor for Bid class.
     * 
     * @param auctionId The auction id of the bid.
     * @param userId    The user id of the bid.
     * @param bid       The bid of the bid.
     * @param isOutbid  The is outbid of the bid.
     */
    public Bid(int auctionId, int userId, int bid, boolean isOutbid) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.bid = bid;
        this.isOutbid = isOutbid;
    }
}
