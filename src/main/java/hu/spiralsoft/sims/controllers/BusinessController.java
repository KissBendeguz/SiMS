package hu.spiralsoft.sims.controllers;

import hu.spiralsoft.sims.entities.Business;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.BusinessRepository;
import hu.spiralsoft.sims.repositories.UserRepository;
import hu.spiralsoft.sims.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;


@Controller
@RequestMapping("/api/v1/business")
@RequiredArgsConstructor
public class BusinessController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private JwtService jwtService;

    @PostMapping("")
    public ResponseEntity<String> createBusiness(@RequestHeader("Authorization") String authHeader,@RequestBody Business request){
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()){
            Business business = Business.builder()
                    .name(request.getName())
                    .owner(userRepository.findByEmail(email).get())
                    .build();
            businessRepository.save(business);
        }else{
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
