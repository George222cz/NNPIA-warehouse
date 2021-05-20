package cz.upce.warehouse;

import cz.upce.warehouse.entity.Product;
import cz.upce.warehouse.entity.Warehouse;
import cz.upce.warehouse.repository.ProductRepository;
import cz.upce.warehouse.repository.WarehouseRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Test
    void saveProductsAndFindProductByNameTest(){
        Warehouse warehouse = new Warehouse();
        warehouse.setAddress("testAddress");
        warehouse.setWarehouseName("Test");
        warehouseRepository.save(warehouse);

        int productCount = productRepository.findAll().size();
        Product product1 = new Product();
        product1.setWarehouse(warehouse);
        product1.setUnitWeight(15);
        product1.setAmount(15);
        product1.setDescription("abc");
        product1.setProductName("ProductTest123");
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setWarehouse(warehouse);
        product2.setUnitWeight(51);
        product2.setAmount(51);
        product2.setDescription("cbd");
        product2.setProductName("ProductTest321");
        productRepository.save(product2);

        List<Product> all = productRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(productCount+2);

        Product test321 = productRepository.findProductByProductNameContains("Test321");
        Assertions.assertThat(test321.getProductName()).contains("Test321");
    }

}
