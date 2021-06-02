package cz.upce.warehouse.service;

import cz.upce.warehouse.dto.WarehouseDto;
import cz.upce.warehouse.entity.Warehouse;
import cz.upce.warehouse.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class WarehouseServiceImpl {
    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> getAllWarehouses(){
        return warehouseRepository.findAll();
    }

    public List<Warehouse> createOrUpdateWarehouse(WarehouseDto dto){
        Warehouse warehouse = new Warehouse();
        warehouse.setId(dto.getId());
        warehouse.setWarehouseName(dto.getWarehouseName());
        warehouse.setAddress(dto.getAddress());
        warehouseRepository.save(warehouse);
        return warehouseRepository.findAll();
    }

    public List<Warehouse> deleteWarehouse(Long warehouseId){
        Optional<Warehouse> byId = warehouseRepository.findById(warehouseId);
        if (byId.isPresent()) {
            warehouseRepository.deleteById(warehouseId);
            return warehouseRepository.findAll();
        } else {
            throw new NoSuchElementException("Warehouse with ID: " + warehouseId + " was not found!");
        }
    }
}
