package cz.upce.warehouse.service;

import cz.upce.warehouse.dto.ProductDto;
import cz.upce.warehouse.entity.Product;
import cz.upce.warehouse.entity.Warehouse;
import cz.upce.warehouse.repository.ProductRepository;
import cz.upce.warehouse.repository.WarehouseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductServiceImpl {
    private final ProductRepository productRepository;

    private final WarehouseRepository warehouseRepository;

    private final TransferFormService transferFormService;

    public ProductServiceImpl(ProductRepository productRepository, WarehouseRepository warehouseRepository, TransferFormService transferFormService) {
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.transferFormService = transferFormService;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Page<Product> getAllProductsPages(Integer page, Integer size){
        if ((page!=null || size!=null) && !(page < 0 || size <= 0)){
            return productRepository.findAll(PageRequest.of(page, size));
        }
        return Page.empty();
    }

    public List<Product> getProductsByWarehouseId(Long warehouseId){
        Optional<Warehouse> warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse.isPresent()) {
            return productRepository.findProductsByWarehouseId(warehouseId);
        } else {
            throw new NoSuchElementException("Warehouse with ID: " + warehouseId + " was not found!");
        }
    }

    public Product getProductById(Long productId){
        if (productRepository.findById(productId).isPresent()) {
            return productRepository.findById(productId).get();
        } else {
            throw new NoSuchElementException("Product with ID: " + productId + " was not found!");
        }
    }

    public ResponseEntity<Object> createOrUpdateProduct(ProductDto dto){
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

    public List<Product> deleteProduct(Long productId){
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            transferFormService.deleteProduct(product.get());
            productRepository.deleteById(productId);
            return productRepository.findAll();
        } else {
            throw new NoSuchElementException("Product with ID: " + productId + " was not found!");
        }
    }
}
