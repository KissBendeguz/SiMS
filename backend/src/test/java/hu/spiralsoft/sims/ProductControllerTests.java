package hu.spiralsoft.sims;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.spiralsoft.sims.entities.Business;
import hu.spiralsoft.sims.entities.Inventory;
import hu.spiralsoft.sims.entities.Product;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.BusinessRepository;
import hu.spiralsoft.sims.repositories.InventoryRepository;
import hu.spiralsoft.sims.repositories.ProductRepository;
import hu.spiralsoft.sims.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTests {
    @MockBean
    @Autowired
    private UserRepository userRepository;
    @MockBean
    @Autowired
    private InventoryRepository inventoryRepository;
    @MockBean
    @Autowired
    private ProductRepository productRepository;
    @MockBean
    @Autowired
    private BusinessRepository businessRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testCreateProducts_InvalidInput_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int inventoryId = 1;
        Product product = Product.builder().build();
        product.setId(1);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/product/"+inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser))
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testDeleteProduct_InvalidInput_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int inventoryId = 1;
        int productId = 1;

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.empty());
        when(productRepository.findById(inventoryId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/product/"+inventoryId+"/"+productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isNotFound());

        verify(inventoryRepository,times(1)).findById(inventoryId);
        verify(productRepository,times(1)).findById(productId);
    }
    @Test
    void testDeleteProduct_ValidInput_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int inventoryId = 1;
        int productId = 1;

        Product product = Product.builder().build();
        product.setId(productId);

        Inventory inventory = Inventory.builder()
                .products(new HashSet<>())
                .build();
        inventory.setId(inventoryId);
        inventory.getProducts().add(product);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        mockMvc.perform(delete("/api/v1/product/"+inventoryId+"/"+productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isOk());

        verify(inventoryRepository,times(1)).findById(inventoryId);
        verify(productRepository,times(1)).findById(productId);
    }
    @Test
    void testModifyProduct_InvalidInput_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int productId = 1;

        Product product = Product.builder().build();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/v1/product/"+productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser))
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());

        verify(productRepository,times(1)).findById(productId);
    }
    @Test
    void testModifyProduct_ValidInput_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int productId = 1;

        Product product = Product.builder().build();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        mockMvc.perform(patch("/api/v1/product/"+productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser))
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());

        verify(productRepository,times(1)).findById(productId);
    }

}
