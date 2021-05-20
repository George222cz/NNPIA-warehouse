package cz.upce.warehouse.controller;

import cz.upce.warehouse.dto.WarehouseDto;
import cz.upce.warehouse.entity.Warehouse;
import cz.upce.warehouse.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/warehouse")
@CrossOrigin
public class WarehouseController {

    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleException(NoSuchElementException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping
    public List<Warehouse> getAllWarehouses(){
        return warehouseRepository.findAll();
    }

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Warehouse> createOrUpdateWarehouse(@RequestBody WarehouseDto dto){
        Warehouse warehouse = new Warehouse();
        warehouse.setId(dto.getId());
        warehouse.setWarehouseName(dto.getWarehouseName());
        warehouse.setAddress(dto.getAddress());
        warehouseRepository.save(warehouse);
        return warehouseRepository.findAll();
    }

    @DeleteMapping("{warehouseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Warehouse> deleteWarehouse(@PathVariable Long warehouseId){
        Optional<Warehouse> byId = warehouseRepository.findById(warehouseId);
        if (byId.isPresent()) {
            warehouseRepository.deleteById(warehouseId);
            return warehouseRepository.findAll();
        } else {
            throw new NoSuchElementException("Warehouse with ID: " + warehouseId + " was not found!");
        }
    }

}
