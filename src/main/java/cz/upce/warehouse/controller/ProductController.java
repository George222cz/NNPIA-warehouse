package cz.upce.warehouse.controller;

import cz.upce.warehouse.dto.AddProductDto;
import cz.upce.warehouse.entity.Product;
import cz.upce.warehouse.entity.Warehouse;
import cz.upce.warehouse.repository.ProductRepository;
import cz.upce.warehouse.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/product")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @GetMapping
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @GetMapping("{productId}")
    public Product getProductById(@PathVariable Long productId){
        if (productRepository.findById(productId).isPresent()) {
            return productRepository.findById(productId).get();
        } else {
            throw new NoSuchElementException("Product with ID: " + productId + " was not found!");
        }
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(AddProductDto dto){
        Optional<Warehouse> warehouse = warehouseRepository.findById(dto.getWarehouse());
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
            throw new NoSuchElementException("Warehouse with ID: " + dto.getWarehouse() + " was not found!");
        }
    }


}
