package cz.upce.warehouse.mockmvc;

import cz.upce.warehouse.WarehouseApplication;
import cz.upce.warehouse.entity.Product;
import cz.upce.warehouse.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = WarehouseApplication.class)
@AutoConfigureMockMvc
public class MockMvcProductTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    public void shouldCreateMockMvc(){
        assertNotNull(mockMvc);
    }

    @Test
    @WithMockUser(username = "admin", password = "pwd", authorities = "ROLE_ADMIN") // ROLE_USER is Forbidden!
    public void getProductByIdTest() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setUnitWeight(15);
        product.setAmount(15);
        product.setDescription("abc");
        product.setProductName("test ProductTest123");
        Optional<Product> optionalProduct = Optional.of(product);

        when(productRepository.findById(1L)).thenReturn(optionalProduct);

        mockMvc.perform(get("/api/product/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productName").value("test ProductTest123"));

    }
}
