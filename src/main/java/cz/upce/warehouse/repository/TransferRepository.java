package cz.upce.warehouse.repository;

import cz.upce.warehouse.dto.TransferStateEnum;
import cz.upce.warehouse.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer,Long> {
    List<Transfer> findAllByState(TransferStateEnum state);
}
