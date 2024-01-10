package hu.spiralsoft.sims.controllers;

import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.UserRepository;
import hu.spiralsoft.sims.security.JwtService;
import hu.spiralsoft.sims.security.http.AuthenticationRequest;
import hu.spiralsoft.sims.security.http.AuthenticationResponse;
import hu.spiralsoft.sims.security.http.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@CrossOrigin
@RequestMapping("/${application.api.ver}/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){

        boolean isValidPassword = request.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
        boolean isValidEmail = request.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");


        if(!isValidEmail || !isValidPassword){
            return ResponseEntity.badRequest().build();
        }else if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .gender(request.getGender())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .associatedBusinesses(new HashSet<>())
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                        .token(token)
                        .build()
        );
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                    .token(token)
                    .build()
        );
    }

    @GetMapping
    public ResponseEntity<User> getAuthenticatedUser(@AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.ok(authenticatedUser);
    }

}
