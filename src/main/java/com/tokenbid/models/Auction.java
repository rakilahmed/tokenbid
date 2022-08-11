package com.tokenbid.models;

import lombok.*;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "auctions")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auctionId;

    @Column(name = "item_id")
    private int itemId;

    @Column(name = "starting_bid")
    private int startingBid;

    @Column(name = "current_bid")
    private int currentBid;

    @Column(name = "previous_bid")
    private int previousBid;

    @Column(name = "status")
    private String status = "In Progress";

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    /**
     * Parameterized constructor for Auction class.
     * 
     * @param itemId      The item id of the auction.
     * @param startingBid The starting bid of the auction.
     * @param startTime   The start time of the auction.
     * @param endTime     The end time of the auction.
     */
    public Auction(int itemId, int startingBid, Timestamp startTime, Timestamp endTime) {
        this.itemId = itemId;
        this.startingBid = startingBid;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Parameterized constructor for Auction class.
     * 
     * @param itemId      The item id of the auction.
     * @param startingBid The starting bid of the auction.
     * @param currentBid  The current bid of the auction.
     * @param previousBid The previous bid of the auction.
     * @param status      The status of the auction.
     * @param startTime   The start time of the auction.
     * @param endTime     The end time of the auction.
     */
    public Auction(int itemId, int startingBid, int currentBid, int previousBid, String status, Timestamp startTime,
            Timestamp endTime) {
        this.itemId = itemId;
        this.startingBid = startingBid;
        this.currentBid = currentBid;
        this.previousBid = previousBid;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Parameterized constructor for Auction class.
     * 
     * @param auctionId   The auction id of the auction.
     * @param itemId      The item id of the auction.
     * @param startingBid The starting bid of the auction.
     * @param currentBid  The current bid of the auction.
     * @param previousBid The previous bid of the auction.
     * @param status      The status of the auction.
     * @param startTime   The start time of the auction.
     * @param endTime     The end time of the auction.
     */
    public Auction(int auctionId, int itemId, int startingBid, int currentBid, int previousBid, String status,
            Timestamp startTime, Timestamp endTime) {
        this.auctionId = auctionId;
        this.itemId = itemId;
        this.startingBid = startingBid;
        this.currentBid = currentBid;
        this.previousBid = previousBid;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }


}
