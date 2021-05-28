package cz.upce.warehouse.repository;

import cz.upce.warehouse.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Product findProductByProductNameContains(String contains);

    List<Product> findProductsByWarehouseId(Long warehouseId);
}
