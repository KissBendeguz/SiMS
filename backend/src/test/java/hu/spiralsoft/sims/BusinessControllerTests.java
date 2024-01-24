package hu.spiralsoft.sims;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.spiralsoft.sims.entities.Business;
import hu.spiralsoft.sims.entities.Inventory;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.BusinessRepository;
import hu.spiralsoft.sims.repositories.InventoryRepository;
import hu.spiralsoft.sims.repositories.UserRepository;
import hu.spiralsoft.sims.security.http.AddEmployeeRequest;
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
import java.util.Set;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BusinessControllerTests {
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
    void testCreateBusiness_ValidInput_ReturnsCreated() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        Business businessRequest = Business.builder()
                .name("Example Business")
                .businessRegistrationDate(new Date())
                .taxNumber("123456789")
                .headQuarters("Example Location")
                .build();

        when(userRepository.findById(authenticatedUser.getId())).thenReturn(Optional.of(authenticatedUser));

        mockMvc.perform(post("/api/v1/businesses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser))
                        .content(objectMapper.writeValueAsString(businessRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(businessRequest.getName()))
                .andExpect(jsonPath("$.owner.email").value(authenticatedUser.getEmail()));

        verify(userRepository,times(1)).findById(authenticatedUser.getId());
    }
    @Test
    void testCreateBusiness_InvalidInput_ReturnsForbidden() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        Business businessRequest = Business.builder()
                .name("Example Business")
                .build();

        mockMvc.perform(post("/api/v1/businesses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser))
                        .content(objectMapper.writeValueAsString(businessRequest)))
                .andExpect(status().isForbidden());
    }
    @Test
    void testGetOwnedBusinesses_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        Set<Business> ownedBusinesses = new HashSet<>();
        ownedBusinesses.add(Business.builder().name("business").owner(authenticatedUser).build());

        when(businessRepository.findByOwner(authenticatedUser)).thenReturn(Optional.of(ownedBusinesses));

        mockMvc.perform(get("/api/v1/businesses/owned")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("business"));

        verify(businessRepository, times(1)).findByOwner(authenticatedUser);
    }
    @Test
    void testGetOwnedBusinesses_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        when(businessRepository.findByOwner(authenticatedUser)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/businesses/owned")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isNotFound());

        verify(businessRepository, times(1)).findByOwner(authenticatedUser);
    }
    @Test
    void testGetAssociatedBusinesses_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);
        authenticatedUser.getAssociatedBusinesses().add(Business.builder().name("test").build());

        mockMvc.perform(get("/api/v1/businesses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isOk());
    }
    @Test
    void testGetAssociatedBusinesses_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        mockMvc.perform(get("/api/v1/businesses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isNotFound());
    }
    @Test
    void testDeleteBusiness_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));

        mockMvc.perform(delete("/api/v1/businesses/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isOk());

        verify(businessRepository,times(1)).findById(businessId);
    }
    @Test
    void testDeleteBusiness_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int businessId = 11;

        mockMvc.perform(delete("/api/v1/businesses/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isNotFound());
    }
    @Test
    void testDeleteBusiness_ReturnsForbidden() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User ownerOfBusiness = User.builder()
                .email("user2@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(2);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(ownerOfBusiness)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));

        mockMvc.perform(delete("/api/v1/businesses/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isForbidden());

        verify(businessRepository,times(1)).findById(businessId);

    }
    @Test
    void testGetBusiness_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);
        business.getAssociates().add(authenticatedUser);

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));

        mockMvc.perform(get("/api/v1/businesses/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isOk());

        verify(businessRepository,times(1)).findById(businessId);
    }
    @Test
    void testGetBusiness_ReturnsForbidden() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));

        mockMvc.perform(get("/api/v1/businesses/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isForbidden());

        verify(businessRepository,times(1)).findById(businessId);
    }
    @Test
    void testGetBusiness_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int businessId = 11;

        when(businessRepository.findById(businessId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/businesses/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))
                .andExpect(status().isNotFound());

        verify(businessRepository,times(1)).findById(businessId);
    }
    @Test
    void testUpdateBusiness_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);
        business.getAssociates().add(authenticatedUser);

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));

        business.setName("updated business");

        mockMvc.perform(get("/api/v1/businesses/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(business))
                        .with(user(authenticatedUser)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(business.getName()));

        verify(businessRepository,times(1)).findById(businessId);
    }
    @Test
    void testUpdateBusiness_ReturnsNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int businessId = 11;

        when(businessRepository.findById(businessId)).thenReturn(Optional.empty());


        mockMvc.perform(get("/api/v1/businesses/"+businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))

                .andExpect(status().isNotFound());

        verify(businessRepository,times(1)).findById(businessId);
    }
    @Test
    void testAddEmployee_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User employee = User.builder()
                .email("employee@example.com")
                .associatedBusinesses(new HashSet<>())
                .build();
        employee.setId(2);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);

        AddEmployeeRequest addEmployeeRequest = AddEmployeeRequest.builder()
                .email(employee.getEmail())
                .homeAddress("")
                .dateOfBirth(new Date())
                .placeOfBirth("")
                .citizenship("")
                .identityCardNumber("")
                .socialSecurityNumber("")
                .phoneNumber("")
                .build();

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(userRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        mockMvc.perform(put("/api/v1/businesses/"+businessId+"/addEmployee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addEmployeeRequest))
                        .with(user(authenticatedUser)))

                .andExpect(status().isOk());

        verify(businessRepository,times(1)).findById(businessId);
        verify(userRepository,times(1)).findByEmail(employee.getEmail());
    }
    @Test
    void testAddEmployee_ReturnsBusinessNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User employee = User.builder()
                .email("employee@example.com")
                .associatedBusinesses(new HashSet<>())
                .build();
        employee.setId(2);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);

        AddEmployeeRequest addEmployeeRequest = AddEmployeeRequest.builder()
                .email(employee.getEmail())
                .homeAddress("")
                .dateOfBirth(new Date())
                .placeOfBirth("")
                .citizenship("")
                .identityCardNumber("")
                .socialSecurityNumber("")
                .phoneNumber("")
                .build();

        when(businessRepository.findById(businessId)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        mockMvc.perform(put("/api/v1/businesses/"+businessId+"/addEmployee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addEmployeeRequest))
                        .with(user(authenticatedUser)))

                .andExpect(status().isNotFound());

        verify(businessRepository,times(1)).findById(businessId);
        verify(userRepository,times(0)).findByEmail(employee.getEmail());
    }
    @Test
    void testAddEmployee_ReturnsEmployeeNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User employee = User.builder()
                .email("employee@example.com")
                .associatedBusinesses(new HashSet<>())
                .build();
        employee.setId(2);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);

        AddEmployeeRequest addEmployeeRequest = AddEmployeeRequest.builder()
                .email(employee.getEmail())
                .homeAddress("")
                .dateOfBirth(new Date())
                .placeOfBirth("")
                .citizenship("")
                .identityCardNumber("")
                .socialSecurityNumber("")
                .phoneNumber("")
                .build();

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(userRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/businesses/"+businessId+"/addEmployee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addEmployeeRequest))
                        .with(user(authenticatedUser)))

                .andExpect(status().isNotFound());

        verify(businessRepository,times(1)).findById(businessId);
        verify(userRepository,times(1)).findByEmail(employee.getEmail());
    }
    @Test
    void testAddEmployee_ReturnsForbidden() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User employee = User.builder()
                .email("employee@example.com")
                .associatedBusinesses(new HashSet<>())
                .build();
        employee.setId(2);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(employee)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);

        AddEmployeeRequest addEmployeeRequest = AddEmployeeRequest.builder()
                .email(employee.getEmail())
                .homeAddress("")
                .dateOfBirth(new Date())
                .placeOfBirth("")
                .citizenship("")
                .identityCardNumber("")
                .socialSecurityNumber("")
                .phoneNumber("")
                .build();

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(userRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/businesses/"+businessId+"/addEmployee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addEmployeeRequest))
                        .with(user(authenticatedUser)))

                .andExpect(status().isForbidden());

        verify(businessRepository,times(1)).findById(businessId);
        verify(userRepository,times(0)).findByEmail(employee.getEmail());
    }
    @Test
    void testRemoveEmployee_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User employee = User.builder()
                .email("employee@example.com")
                .associatedBusinesses(new HashSet<>())
                .build();
        employee.setId(2);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);
        business.getAssociates().add(employee);

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(userRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        mockMvc.perform(put("/api/v1/businesses/"+businessId+"/removeEmployee/"+employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))

                .andExpect(status().isOk());

        verify(businessRepository,times(1)).findById(businessId);
        verify(userRepository,times(1)).findById(employee.getId());
    }
    @Test
    void testRemoveEmployee_ReturnsBusinessNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User employee = User.builder()
                .email("employee@example.com")
                .associatedBusinesses(new HashSet<>())
                .build();
        employee.setId(2);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);
        business.getAssociates().add(employee);

        when(businessRepository.findById(businessId)).thenReturn(Optional.empty());
        when(userRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        mockMvc.perform(put("/api/v1/businesses/"+businessId+"/removeEmployee/"+employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))

                .andExpect(status().isNotFound());

        verify(businessRepository,times(1)).findById(businessId);
        verify(userRepository,times(0)).findById(employee.getId());
    }
    @Test
    void testRemoveEmployee_ReturnsEmployeeNotFound() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User employee = User.builder()
                .email("employee@example.com")
                .associatedBusinesses(new HashSet<>())
                .build();
        employee.setId(2);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(authenticatedUser)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);
        business.getAssociates().add(employee);

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(userRepository.findById(employee.getId())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/businesses/"+businessId+"/removeEmployee/"+employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))

                .andExpect(status().isNotFound());

        verify(businessRepository,times(1)).findById(businessId);
        verify(userRepository,times(1)).findById(employee.getId());
    }
    @Test
    void testRemoveEmployee_ReturnsForbidden() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        User employee = User.builder()
                .email("employee@example.com")
                .associatedBusinesses(new HashSet<>())
                .build();
        employee.setId(2);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .owner(employee)
                .associates(new HashSet<>())
                .build();
        business.setId(businessId);
        business.getAssociates().add(employee);

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(userRepository.findById(employee.getId())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/businesses/"+businessId+"/removeEmployee/"+employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))

                .andExpect(status().isForbidden());

        verify(businessRepository,times(1)).findById(businessId);
        verify(userRepository,times(0)).findById(employee.getId());
    }
    @Test
    void testGetBusinessInventories_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .build();
        business.setId(businessId);

        Inventory inventory1 = Inventory.builder()
                .business(business)
                .build();
        inventory1.setId(1);

        Inventory inventory2 = Inventory.builder()
                .business(business)
                .build();
        inventory2.setId(2);

        Set<Inventory> inventories = new HashSet<>();
        inventories.add(inventory1);
        inventories.add(inventory2);

        when(inventoryRepository.findAllByBusinessId(businessId)).thenReturn(Optional.of(inventories));

        mockMvc.perform(get("/api/v1/businesses/"+businessId+"/inventories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))

                .andExpect(status().isOk());

        verify(inventoryRepository,times(1)).findAllByBusinessId(businessId);
    }
    @Test
    void testGetBusinessInventories_ReturnsNotFoundk() throws Exception {
        User authenticatedUser = User.builder()
                .email("user@example.com")
                .password("password")
                .associatedBusinesses(new HashSet<>())
                .build();
        authenticatedUser.setId(1);

        int businessId = 11;
        Business business = Business.builder()
                .name("test")
                .build();
        business.setId(businessId);

        when(inventoryRepository.findAllByBusinessId(businessId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/businesses/"+businessId+"/inventories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(authenticatedUser)))

                .andExpect(status().isNotFound());

        verify(inventoryRepository,times(1)).findAllByBusinessId(businessId);
    }
}
