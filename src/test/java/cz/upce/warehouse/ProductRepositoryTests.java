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
import org.springframework.boot.test.context.SpringBootTest;
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
    void saveProductTest() {
        Product product = new Product();
        product.setProductName("test");
        productRepository.save(product);

        List<Product> all = productRepository.findAll();

        Assertions.assertThat(all.size()).isEqualTo(1);

        //  Product product123 = productRepository.findProductByProductNameContains("123");
    }

    @Test
    void findAllProductsTest(){
        Warehouse warehouse = new Warehouse();
        warehouse.setAddress("testAddress");
        warehouse.setWarehouseName("Test");
        warehouseRepository.save(warehouse);

        Product product = new Product();
        product.setWarehouse(warehouse);
        product.setUnitWeight(10);
        product.setAmount(10);
        product.setDescription("abc");
        product.setProductName("ProductTest");
        productRepository.save(product);

        List<Product> all = productRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);

    }

}
