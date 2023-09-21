package hu.spiralsoft.sims.controllers;

import hu.spiralsoft.sims.entities.Business;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.BusinessRepository;
import hu.spiralsoft.sims.repositories.UserRepository;
import hu.spiralsoft.sims.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/businesses")
@RequiredArgsConstructor
public class BusinessController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private JwtService jwtService;

    @PostMapping("")
    public ResponseEntity<Business> createBusiness(@AuthenticationPrincipal User authenticatedUser,@RequestBody Business body){
        if (body.getName().length() < 3 || body.getName().length() > 26){
            return ResponseEntity.badRequest().build();
        }
        Business business = Business.builder()
                .name(body.getName())
                .owner(authenticatedUser)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(businessRepository.save(business));
    }

    @GetMapping("/owned")
    public ResponseEntity<Set<Business>> getOwnedBusinesses(@AuthenticationPrincipal User authenticatedUser){
        Optional<Set<Business>> businesses = businessRepository.findByOwner(authenticatedUser);

        if (businesses.isPresent() && !businesses.get().isEmpty()){
            return ResponseEntity.ok(businesses.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //TODO implement getAssociatedBusinesses()
    @GetMapping("")
    public ResponseEntity<Set<Business>> getAssociatedBusinesses(@AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBusiness(@AuthenticationPrincipal User authenticatedUser,@PathVariable int id){
        if (businessRepository.findById(id).orElseThrow().getOwner().equals(authenticatedUser)){
            businessRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Business> updateBusiness(@AuthenticationPrincipal User authenticatedUser,@PathVariable Integer id,@RequestBody Business body){
        Business business = businessRepository.findById(id).orElseThrow();
        if (business.getOwner().equals(authenticatedUser)){

            business.setName(body.getName());

            businessRepository.save(business);
            return ResponseEntity.ok(business);
        }
        return ResponseEntity.notFound().build();
    }
    @PatchMapping("/{id}/join")
    public ResponseEntity<Business> joinBusiness(@AuthenticationPrincipal User authenticatedUser,@PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
