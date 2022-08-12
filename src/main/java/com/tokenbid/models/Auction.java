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
}
