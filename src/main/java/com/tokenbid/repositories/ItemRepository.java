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
     * Finds all available items that are not currently being auctioned
     * 
     * @param userId The userId representing the user to search for
     * @return A list of available items
     */
    @Query(value = "SELECT * FROM items WHERE user_id = :userId AND item_id NOT IN (SELECT item_id FROM auctions WHERE status = 'In Progress')", nativeQuery = true)
    List<Item> findAllAvailableItems(@Param("userId") int userId);
}
