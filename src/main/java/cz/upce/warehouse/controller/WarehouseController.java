package cz.upce.warehouse.controller;

import cz.upce.warehouse.dto.WarehouseDto;
import cz.upce.warehouse.entity.Warehouse;
import cz.upce.warehouse.service.WarehouseServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/api/warehouse")
public class WarehouseController {

    private final WarehouseServiceImpl warehouseService;

    public WarehouseController(WarehouseServiceImpl warehouseService) {
        this.warehouseService = warehouseService;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleException(NoSuchElementException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping
    public List<Warehouse> getAllWarehouses(){
        return warehouseService.getAllWarehouses();
    }

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Warehouse> createOrUpdateWarehouse(@RequestBody WarehouseDto dto){
        return warehouseService.createOrUpdateWarehouse(dto);
    }

    @DeleteMapping("{warehouseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Warehouse> deleteWarehouse(@PathVariable Long warehouseId){
        return warehouseService.deleteWarehouse(warehouseId);
    }

}
