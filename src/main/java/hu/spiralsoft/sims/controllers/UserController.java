package hu.spiralsoft.sims.controllers;

import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.UserRepository;
import hu.spiralsoft.sims.security.JwtService;
import hu.spiralsoft.sims.security.http.AuthenticationRequest;
import hu.spiralsoft.sims.security.http.AuthenticationResponse;
import hu.spiralsoft.sims.security.http.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        User user = User.builder()
                //.firstname(request.getFirstname())
                //.lastname(request.getLastname())
                //.middlename(request.getMiddlename())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(
                AuthenticationResponse.builder().
                        token(token).
                        build()
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
            //CHECK EXPIRED SHIT
        System.out.println("login ");
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                    .token(token)
                    .build()
        );
    }
}
