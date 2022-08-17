package com.tokenbid.services;

import java.util.List;

import com.tokenbid.models.Auction;
import com.tokenbid.repositories.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tokenbid.models.Item;
import com.tokenbid.repositories.ItemRepository;

@Service
public class ItemService implements IService<Item> {
    private ItemRepository itemRepository;
    private AuctionRepository auctionRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, AuctionRepository auctionRepository) {
        this.itemRepository = itemRepository;
        this.auctionRepository = auctionRepository;
    }

    @Override
    public int add(Item item) {
        return itemRepository.save(item).getItemId();
    }

    @Override
    public void update(Item item) {
        if (itemRepository.findById(item.getItemId()).isPresent()) {
            itemRepository.save(item);
        }
    }

    @Override
    public void delete(int id) {
        if (itemRepository.findById(id).isPresent()) {
            itemRepository.deleteById(id);
        }
    }

    @Override
    public Item getById(int id) {
        if (itemRepository.findById(id).isPresent()) {
            return itemRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    /**
     * @param itemId Item to search for
     * @return An auction associated to an item or null if there is none
     */
    public Auction getItemAuction(int itemId) {
        return auctionRepository.findByItemId(itemId);
    }

    public List<Item> getAllForUser(int userId) {
        return itemRepository.findAllByUserId(userId);
    }

}
