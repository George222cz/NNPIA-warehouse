package cz.upce.warehouse.repository;

import cz.upce.warehouse.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

//    @EntityGraph(attributePaths = {"productsInTransfers"})
    Product findProductByProductNameContains(String contains);

 //   @EntityGraph(attributePaths = {"productsInTransfers"})
    List<Product> findProductsByAmountEquals(Integer amount);
}
