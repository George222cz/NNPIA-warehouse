package cz.upce.warehouse.controller;

import cz.upce.warehouse.dto.ProductDto;
import cz.upce.warehouse.entity.Product;
import cz.upce.warehouse.entity.Warehouse;
import cz.upce.warehouse.repository.ProductRepository;
import cz.upce.warehouse.repository.WarehouseRepository;
import cz.upce.warehouse.service.TransferFormService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {

    private final ProductRepository productRepository;

    private final WarehouseRepository warehouseRepository;

    private final TransferFormService transferFormService;

    public ProductController(ProductRepository productRepository, WarehouseRepository warehouseRepository, TransferFormService transferFormService) {
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.transferFormService = transferFormService;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleException(){
         return ResponseEntity.badRequest().body("{\"message\":\"Cannot delete: This product is used in transfers!\"}");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleException(NoSuchElementException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<Product> getProductsByWarehouseId(@PathVariable Long warehouseId){
        Optional<Warehouse> warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse.isPresent()) {
            return productRepository.findProductsByWarehouseId(warehouseId);
        } else {
            throw new NoSuchElementException("Warehouse with ID: " + warehouseId + " was not found!");
        }
    }

    @GetMapping("{productId}")
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public Product getProductById(@PathVariable Long productId){
        if (productRepository.findById(productId).isPresent()) {
            return productRepository.findById(productId).get();
        } else {
            throw new NoSuchElementException("Product with ID: " + productId + " was not found!");
        }
    }

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createOrUpdateProduct(@RequestBody ProductDto dto){
        Optional<Warehouse> warehouse = warehouseRepository.findById(dto.getWarehouseId());
        if (warehouse.isPresent()) {
            Product product = new Product();
            product.setId(dto.getId());
            product.setProductName(dto.getProductName());
            product.setDescription(dto.getDescription());
            product.setUnitWeight(dto.getUnitWeight());
            product.setAmount(dto.getAmount());
            product.setWarehouse(warehouse.get());
            productRepository.save(product);
            return ResponseEntity.ok().build();
        } else {
            throw new NoSuchElementException("Warehouse with ID: " + dto.getWarehouseId() + " was not found!");
        }
    }

    @DeleteMapping("{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Product> deleteProduct(@PathVariable Long productId){
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            transferFormService.getTransferForm().remove(product.get());
            productRepository.deleteById(productId);
            return productRepository.findAll();
        } else {
            throw new NoSuchElementException("Product with ID: " + productId + " was not found!");
        }
    }

}
