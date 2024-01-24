package hu.spiralsoft.sims;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.spiralsoft.sims.entities.Business;
import hu.spiralsoft.sims.entities.Inventory;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.BusinessRepository;
import hu.spiralsoft.sims.repositories.InventoryRepository;
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
class InventoryControllerTests {
    @MockBean
    @Autowired
    private UserRepository userRepository;
    @MockBean
    @Autowired
    private InventoryRepository inventoryRepository;
    @MockBean
    @Autowired
    private BusinessRepository businessRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testCreateInventory_ValidInput_ReturnsCreated() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int businessId = 1;

        Business business = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .owner(authenticatedUser)
                .build();

        Inventory inventory = Inventory.builder()
                .name("")
                .address("")
                .managerName("")
                .managerPhone("")
                .managerEmail("")
                .build();

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));

        mockMvc.perform(post("/api/v1/inventory/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser))
                        .content(objectMapper.writeValueAsString(inventory)))
                .andExpect(status().isCreated());

        verify(businessRepository,times(1)).findById(businessId);
    }
    @Test
    void testCreateInventory_ValidInput_ReturnsForbidden() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User ownerOfBusiness = User.builder().build();
        ownerOfBusiness.setId(2);

        int businessId = 1;

        Business business = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .owner(ownerOfBusiness)
                .build();

        Inventory inventory = Inventory.builder()
                .name("")
                .address("")
                .managerName("")
                .managerPhone("")
                .managerEmail("")
                .build();

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));

        mockMvc.perform(post("/api/v1/inventory/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser))
                        .content(objectMapper.writeValueAsString(inventory)))
                .andExpect(status().isForbidden());

        verify(businessRepository,times(1)).findById(businessId);
    }
    @Test
    void testCreateInventory_InvalidInput_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int businessId = 1;

        Business business = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .build();

        Inventory inventory = Inventory.builder()
                .name("")
                .address("")
                .managerName("")
                .managerPhone("")
                .managerEmail("")
                .build();

        when(businessRepository.findById(businessId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/inventory/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser))
                        .content(objectMapper.writeValueAsString(inventory)))
                .andExpect(status().isNotFound());

        verify(businessRepository,times(1)).findById(businessId);
    }
    @Test
    void testCreateInventory_InvalidInput_ReturnsBadRequest() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);
        int businessId = 1;

        mockMvc.perform(post("/api/v1/inventory/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testDeleteInventory_ValidInput_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int inventoryId = 1;
        int businessId = 1;
        Business business = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .owner(authenticatedUser)
                .build();
        business.setId(businessId);

        Inventory inventory = Inventory.builder()
                .name("")
                .address("")
                .managerName("")
                .managerPhone("")
                .managerEmail("")
                .business(business)
                .build();
        inventory.setId(inventoryId);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        mockMvc.perform(delete("/api/v1/inventory/"+inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isOk());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testDeleteInventory_ValidInput_ReturnsForbidden() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User ownerOfBusiness = User.builder().build();
        ownerOfBusiness.setId(2);

        int inventoryId = 1;
        int businessId = 1;
        Business business = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .owner(ownerOfBusiness)
                .build();
        business.setId(businessId);

        Inventory inventory = Inventory.builder()
                .name("")
                .address("")
                .managerName("")
                .managerPhone("")
                .managerEmail("")
                .business(business)
                .build();
        inventory.setId(inventoryId);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        mockMvc.perform(delete("/api/v1/inventory/"+inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isForbidden());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testDeleteInventory_InvalidInput_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int inventoryId = 1;

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/inventory/"+inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isNotFound());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testGetInventory_Valid_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int inventoryId = 1;
        int businessId = 1;

        Business business = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);
        business.getAssociates().add(authenticatedUser);

        Inventory inventory = Inventory.builder()
                .name("")
                .address("")
                .managerName("")
                .managerPhone("")
                .managerEmail("")
                .business(business)
                .build();
        inventory.setId(inventoryId);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        mockMvc.perform(get("/api/v1/inventory/"+inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isOk());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testGetInventory_InvalidInput_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int inventoryId = 1;

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/inventory/"+inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isNotFound());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testGetInventory_Valid_ReturnsForbidden() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int inventoryId = 1;
        int businessId = 1;

        Business business = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);

        Inventory inventory = Inventory.builder()
                .name("")
                .address("")
                .managerName("")
                .managerPhone("")
                .managerEmail("")
                .business(business)
                .build();
        inventory.setId(inventoryId);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        mockMvc.perform(get("/api/v1/inventory/"+inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isForbidden());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testModifyInventory_InvalidInput_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int inventoryId = 1;
        Inventory inventory = Inventory.builder().build();
        inventory.setId(inventoryId);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/v1/inventory/"+inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser))
                        .content(objectMapper.writeValueAsString(inventory)))
                .andExpect(status().isNotFound());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testModifyInventory_ValidInput_ReturnsForbidden() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User ownerOfBusiness = User.builder().build();
        ownerOfBusiness.setId(2);

        int inventoryId = 1;
        int businessId = 1;

        Business business = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .owner(ownerOfBusiness)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);

        Inventory inventory = Inventory.builder()
                .name("")
                .address("")
                .managerName("")
                .managerPhone("")
                .managerEmail("")
                .business(business)
                .build();
        inventory.setId(inventoryId);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        inventory.setName("New name");

        mockMvc.perform(patch("/api/v1/inventory/"+inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser))
                        .content(objectMapper.writeValueAsString(inventory)))
                .andExpect(status().isForbidden());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testModifyInventory_ValidInput_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);


        int inventoryId = 1;
        int businessId = 1;

        Business business = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);

        Inventory inventory = Inventory.builder()
                .name("")
                .address("")
                .managerName("")
                .managerPhone("")
                .managerEmail("")
                .business(business)
                .build();
        inventory.setId(inventoryId);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        inventory.setName("New name");

        mockMvc.perform(patch("/api/v1/inventory/"+inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser))
                        .content(objectMapper.writeValueAsString(inventory)))
                .andExpect(status().isOk());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testGetAllProducts_InvalidInput_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int inventoryId = 1;

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/inventory/"+inventoryId+"/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isNotFound());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testGetAllProducts_ValidInput_ReturnsForbidden() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User ownerOfBusiness = User.builder().build();
        ownerOfBusiness.setId(2);

        int inventoryId = 1;
        int businessId = 1;

        Business business = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .owner(ownerOfBusiness)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);

        Inventory inventory = Inventory.builder()
                .name("")
                .address("")
                .managerName("")
                .managerPhone("")
                .managerEmail("")
                .business(business)
                .build();
        inventory.setId(inventoryId);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        mockMvc.perform(get("/api/v1/inventory/"+inventoryId+"/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isForbidden());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }
    @Test
    void testGetAllProducts_ValidInput_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User ownerOfBusiness = User.builder().build();
        ownerOfBusiness.setId(2);

        int inventoryId = 1;
        int businessId = 1;

        Business business = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .owner(ownerOfBusiness)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);
        business.getAssociates().add(authenticatedUser);

        Inventory inventory = Inventory.builder()
                .name("")
                .address("")
                .managerName("")
                .managerPhone("")
                .managerEmail("")
                .business(business)
                .build();
        inventory.setId(inventoryId);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        mockMvc.perform(get("/api/v1/inventory/"+inventoryId+"/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isOk());

        verify(inventoryRepository,times(1)).findById(inventoryId);
    }

}