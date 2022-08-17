package com.tokenbid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tokenbid.models.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    /**
     * Finds all items belonging to a user
     *
     * @param userId The userId representing the user to search for
     * @return A list of items belonging to a user
     */
    List<Item> findAllByUserId(int userId);
}
