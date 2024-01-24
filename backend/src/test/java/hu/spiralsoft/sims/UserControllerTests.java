package hu.spiralsoft.sims;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.UserRepository;
import hu.spiralsoft.sims.security.http.AuthenticationRequest;
import hu.spiralsoft.sims.security.http.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static hu.spiralsoft.sims.entities.Gender.MALE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {
    @MockBean
    @Autowired
    private UserRepository userRepository;
    @MockBean
    @Autowired
    private AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegister_ValidRequest_ReturnsOk() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .password("Passwd12345")
                .taxNumber("123456")
                .gender(MALE)
                .build();

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());


        verify(userRepository,times(1)).findByEmail(any());
    }
    @Test
    void testRegister_ValidRequest_Returns_Conflict() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .password("Passwd12345")
                .taxNumber("123456")
                .gender(MALE)
                .build();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));

        mockMvc.perform(post("/api/v1/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().is(409));


        verify(userRepository,times(1)).findByEmail(any());
    }
    @Test
    void testLogin_ValidRequest_ReturnsOk() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email("john@example.com")
                .password("password")
                .build();
        User authenticatedUser = User.builder()
                .email(authenticationRequest.getEmail())
                .password(passwordEncoder.encode(authenticationRequest.getPassword()))
                .build();
        authenticatedUser.setId(1);

        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(authenticatedUser));

        mockMvc.perform(post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk());

        verify(authenticationManager, times(1)).authenticate(any());
    }
    @Test
    void testLogin_ValidRequest_ReturnsUnauthorized() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email("john@example.com")
                .password("wrongPassword")
                .build();

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isUnauthorized());

        verify(authenticationManager, times(1)).authenticate(any());
    }
    @Test
    void testGetAuthenticatedUser_ReturnsOk() throws Exception {
        User authenticatedUser = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .password("password")
                .gender(MALE)
                .build();

        mockMvc.perform(get("/api/v1/user")
                .with(user(authenticatedUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
