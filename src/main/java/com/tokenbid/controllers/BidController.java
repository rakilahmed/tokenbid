package com.tokenbid.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tokenbid.models.Bid;
import com.tokenbid.services.BidService;

@RestController
@RequestMapping("/bids")
public class BidController implements IController<Bid> {
    private BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @Override
    @PostMapping(path = "/add", consumes = "application/json")
    public ResponseEntity<Boolean> add(@RequestBody Bid bid) throws URISyntaxException {
        return ResponseEntity.created(new URI("/bids/" + bidService.add(bid))).build();
    }

    @Override
    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Boolean> update(@PathVariable("id") int id, @RequestBody Bid updatedBid) {
        if (bidService.getById(id) != null) {
            updatedBid.setBidId(id);
            bidService.update(updatedBid);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") int id) {
        if (bidService.getById(id) != null) {
            bidService.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Bid> get(@PathVariable("id") int id) {
        Bid bid = bidService.getById(id);
        if (bid != null) {
            return ResponseEntity.ok(bid);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity<List<Bid>> getAll() {
        return ResponseEntity.ok(bidService.getAll());
    }

    /**
     * To get the highest bid and corresponding user id by auctionID
     * @param auctionId
     * @return Bid object with the highest bid and user id as per Auction ID
     */
    @GetMapping(path = "/highest-bid/{id}", produces = "application/json")
    public ResponseEntity<Bid> getHighestBidAndUserId(@PathVariable("id") int auctionId){
        return ResponseEntity.ok(bidService.getHighestBidForAnAuction(auctionId));
    }
}
