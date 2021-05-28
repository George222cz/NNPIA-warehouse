package cz.upce.warehouse.mockmvc;

import cz.upce.warehouse.WarehouseApplication;
import cz.upce.warehouse.entity.Warehouse;
import cz.upce.warehouse.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = WarehouseApplication.class)
@AutoConfigureMockMvc
public class MockMvcWarehouseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    public void shouldCreateMockMvc(){
        assertNotNull(mockMvc);
    }

    @Test
    @WithMockUser(username = "admin", password = "pwd", authorities = "ROLE_ADMIN")
    public void getAllWarehousesTest() throws Exception {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setAddress("test address123");
        warehouse.setWarehouseName("test");
        when(warehouseRepository.findAll()).thenReturn(Arrays.asList(warehouse));

        mockMvc.perform(get("/api/warehouse")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].address").value("test address123"));
    }
}
