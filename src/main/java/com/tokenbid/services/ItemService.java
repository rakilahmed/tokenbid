package com.tokenbid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tokenbid.models.Item;
import com.tokenbid.repositories.ItemRepository;

@Service
public class ItemService extends IService<Item> {
    private ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
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

}
