package cz.upce.warehouse.controller;

import cz.upce.warehouse.dto.ProductDto;
import cz.upce.warehouse.entity.Product;
import cz.upce.warehouse.service.ProductServiceImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {

    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
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
        return productService.getAllProducts();
    }

    @GetMapping("/pages")
    public Page<Product> getAllProductsPages(@RequestParam Integer page, @RequestParam Integer size){
        return productService.getAllProductsPages(page, size);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<Product> getProductsByWarehouseId(@PathVariable Long warehouseId){
        return productService.getProductsByWarehouseId(warehouseId);
    }

    @GetMapping("{productId}")
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public Product getProductById(@PathVariable Long productId){
        return productService.getProductById(productId);
    }

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createOrUpdateProduct(@RequestBody ProductDto dto){
        return productService.createOrUpdateProduct(dto);
    }

    @DeleteMapping("{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Product> deleteProduct(@PathVariable Long productId){
        return productService.deleteProduct(productId);
    }

}
