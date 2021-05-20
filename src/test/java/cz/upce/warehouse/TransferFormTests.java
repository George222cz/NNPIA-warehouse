package cz.upce.warehouse;

import cz.upce.warehouse.datafactory.Creator;
import cz.upce.warehouse.entity.Product;
import cz.upce.warehouse.entity.Warehouse;
import cz.upce.warehouse.repository.ProductRepository;
import cz.upce.warehouse.repository.WarehouseRepository;
import cz.upce.warehouse.service.TransferFormService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.google.common.collect.Iterables.getLast;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"cz.upce.warehouse.service"})
@Import(Creator.class)
public class TransferFormTests {

    @Autowired
    private TransferFormService transferFormService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    void addAndRemoveProductToTransferFormTest() {
        Warehouse warehouse = new Warehouse();
        warehouse.setAddress("testAddress");
        warehouse.setWarehouseName("Test");
        warehouseRepository.save(warehouse);

        int productCount = productRepository.findAll().size();
        Product product = new Product();
        product.setWarehouse(warehouse);
        product.setUnitWeight(10);
        product.setAmount(10);
        product.setDescription("abc");
        product.setProductName("ProductTest");
        productRepository.save(product);

        List<Product> all = productRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(productCount+1);

        Long productId = getLast(all).getId();
        transferFormService.add(productId);

        Assertions.assertThat(transferFormService.getTransferForm().size()).isEqualTo(1);

        Assertions.assertThat(transferFormService.getTransferForm().containsKey(getLast(all))).isTrue();

        Assertions.assertThat(transferFormService.getTransferForm().get(getLast(all))).isEqualTo(1);

        transferFormService.add(productId);
        Assertions.assertThat(transferFormService.getTransferForm().get(getLast(all))).isEqualTo(2);

        transferFormService.add(productId);
        Assertions.assertThat(transferFormService.getTransferForm().get(getLast(all))).isEqualTo(3);

        transferFormService.remove(productId);
        Assertions.assertThat(transferFormService.getTransferForm().get(getLast(all))).isEqualTo(2);

        transferFormService.remove(productId);
        Assertions.assertThat(transferFormService.getTransferForm().get(getLast(all))).isEqualTo(1);

        transferFormService.remove(productId);
        Assertions.assertThat(transferFormService.getTransferForm().containsKey(getLast(all))).isFalse();
    }
}
