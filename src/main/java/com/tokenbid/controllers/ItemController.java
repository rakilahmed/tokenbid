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

import com.tokenbid.models.Item;
import com.tokenbid.services.ItemService;

@RestController
@RequestMapping("/items")
public class ItemController implements IController<Item> {
    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    @PostMapping(path = "/add", consumes = "application/json")
    public ResponseEntity<String> add(@RequestBody Item item) throws URISyntaxException {
        return ResponseEntity.created(new URI("/items/" + itemService.add(item))).build();
    }

    @Override
    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<String> update(@PathVariable("id") int id, @RequestBody Item updatedItem) {
        if (itemService.getById(id) != null) {
            updatedItem.setItemId(id);
            itemService.update(updatedItem);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        if (itemService.getById(id) != null) {
            itemService.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<Item> get(@PathVariable("id") int id) {
        Item item = itemService.getById(id);
        if (item != null) {
            return ResponseEntity.ok(item);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity<List<Item>> getAll() {
        return ResponseEntity.ok(itemService.getAll());
    }

    @GetMapping(path = "/{userId}/available", produces = "application/json")
    public ResponseEntity<List<Item>> getAllAvailableItems(@PathVariable("userId") int userId) {
        return ResponseEntity.ok(itemService.getAllAvailableItems(userId));
    }
}
