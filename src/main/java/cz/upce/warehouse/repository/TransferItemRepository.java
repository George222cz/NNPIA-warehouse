package cz.upce.warehouse.repository;

import cz.upce.warehouse.entity.TransferItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferItemRepository extends JpaRepository<TransferItem,Long> {

}
