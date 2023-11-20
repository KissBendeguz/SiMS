package hu.spiralsoft.sims.controllers;

import hu.spiralsoft.sims.entities.Business;
import hu.spiralsoft.sims.entities.Inventory;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.BusinessRepository;
import hu.spiralsoft.sims.repositories.InventoryRepository;
import hu.spiralsoft.sims.repositories.UserRepository;
import hu.spiralsoft.sims.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/${application.api.ver}/businesses")
@RequiredArgsConstructor
public class BusinessController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private JwtService jwtService;

    @PostMapping("")
    public ResponseEntity<Business> createBusiness(@AuthenticationPrincipal User authenticatedUser,@RequestBody Business body){
        if (body == null || body.getName().length() < 3 || body.getName().length() > 26){
            return ResponseEntity.badRequest().build();
        }
        Optional<User> savedUser = userRepository.findById(authenticatedUser.getId());
        if (savedUser.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        Business business = Business.builder()
                .name(body.getName())
                .owner(savedUser.get())
                .associates(new HashSet<>())
                .build();
        businessRepository.save(business);
        savedUser.get().getAssociatedBusinesses().add(business);
        userRepository.save(savedUser.get());
        business.getAssociates().add(savedUser.get());
        businessRepository.save(business);

        return ResponseEntity.status(HttpStatus.CREATED).body(business);
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

    @GetMapping("")
    public ResponseEntity<Set<Business>> getAssociatedBusinesses(@AuthenticationPrincipal User authenticatedUser){
        if(authenticatedUser.getAssociatedBusinesses()!=null){
            return ResponseEntity.ok(authenticatedUser.getAssociatedBusinesses());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBusiness(@AuthenticationPrincipal User authenticatedUser, @PathVariable Integer id) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if (optionalBusiness.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Business business = optionalBusiness.get();

        if (business.getOwner() != null && business.getOwner().getId().equals(authenticatedUser.getId())) {
            authenticatedUser.getAssociatedBusinesses().remove(business);
            userRepository.save(authenticatedUser);
            businessRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Business> updateBusiness(@AuthenticationPrincipal User authenticatedUser,@PathVariable Integer id,@RequestBody Business body){
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if(optionalBusiness.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Business business = optionalBusiness.get();
        if (business.getOwner().equals(authenticatedUser)){

            business.setName(body.getName());

            businessRepository.save(business);
            return ResponseEntity.ok(business);
        }
        return ResponseEntity.notFound().build();
    }
    @PatchMapping("/{id}/join")
    public ResponseEntity<Business> joinBusiness(@AuthenticationPrincipal User authenticatedUser,@PathVariable Integer id){
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if(optionalBusiness.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Business business = optionalBusiness.get();
        authenticatedUser.getAssociatedBusinesses().add(business);
        userRepository.save(authenticatedUser);
        business.getAssociates().add(authenticatedUser);
        businessRepository.save(business);
        return ResponseEntity.ok(business);
    }

    @GetMapping("/{id}/inventories")
    public ResponseEntity<Set<Inventory>> getBusinessInventories(@AuthenticationPrincipal User authenticatedUser,@PathVariable Integer id){
        Optional<Set<Inventory>> oInventories = inventoryRepository.findAllByBusinessId(id);
        if (oInventories.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(oInventories.get());
    }
}