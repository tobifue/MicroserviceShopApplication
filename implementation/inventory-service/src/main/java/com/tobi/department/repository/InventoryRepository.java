package com.tobi.department.repository;

import com.tobi.department.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * InventoryRepository extends JpaRepo
 */
@Repository
public interface InventoryRepository extends JpaRepository<Item, Long> {

    Item findByItemId(Long ItemId);

}
