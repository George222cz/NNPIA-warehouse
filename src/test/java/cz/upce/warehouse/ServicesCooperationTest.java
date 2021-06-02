package cz.upce.warehouse;

import cz.upce.warehouse.datafactory.Creator;
import cz.upce.warehouse.entity.Product;
import cz.upce.warehouse.entity.Transfer;
import cz.upce.warehouse.entity.User;
import cz.upce.warehouse.repository.ProductRepository;
import cz.upce.warehouse.repository.TransferRepository;
import cz.upce.warehouse.repository.UserRepository;
import cz.upce.warehouse.service.TransferFormService;
import cz.upce.warehouse.service.UserDetailsImpl;
import cz.upce.warehouse.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.assertj.core.api.Assertions;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Iterables.getLast;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"cz.upce.warehouse.service"})
@Import(Creator.class)
public class ServicesCooperationTest {

    @Autowired
    private Creator creator;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransferFormService transferFormService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getUserDetailsAndUseThemForTransferCreationTest(){
        // test user creation
        int userCount = userRepository.findAll().size();
        creator.saveEntity(new User());
        List<User> users = userRepository.findAll();
        Assertions.assertThat(users.size()).isEqualTo(userCount+1);

        // getting user details from service
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(getLast(users).getUsername());
        Assertions.assertThat(userDetails.getUsername()).isEqualTo("Test username");

        // test product creation
        int productCount = productRepository.findAll().size();
        creator.saveEntity(new Product());
        List<Product> products = productRepository.findAll();
        Assertions.assertThat(products.size()).isEqualTo(productCount+1);

        // adding test product to transfer-form service
        Product product = getLast(products);
        Assertions.assertThat(transferFormService.getTransferForm().size()).isEqualTo(0);
        transferFormService.add(product.getId());
        transferFormService.add(product.getId());
        Assertions.assertThat(transferFormService.getTransferForm().size()).isEqualTo(1);
        Assertions.assertThat(getLast(transferFormService.getTransferForm()).getAmount()).isEqualTo(2);

        // confirm of transfer-form by user and test address
        Optional<User> user = userRepository.findById(userDetails.getId());
        Assertions.assertThat(user.isPresent()).isEqualTo(true);
        int transferCount = transferRepository.findAll().size();
        transferFormService.confirm(user.get(),"test address123");
        Assertions.assertThat(transferFormService.getTransferForm().size()).isEqualTo(0);

        // test of new transfer creation
        List<Transfer> transfers = transferRepository.findAll();
        Assertions.assertThat(transfers.size()).isEqualTo(transferCount+1);
        Assertions.assertThat(getLast(transfers).getAddress()).isEqualTo("test address123");
    }

}
