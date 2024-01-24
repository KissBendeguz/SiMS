package hu.spiralsoft.sims.controllers;

import hu.spiralsoft.sims.entities.Business;
import hu.spiralsoft.sims.entities.Inventory;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.BusinessRepository;
import hu.spiralsoft.sims.repositories.InventoryRepository;
import hu.spiralsoft.sims.repositories.UserRepository;
import hu.spiralsoft.sims.security.JwtService;
import hu.spiralsoft.sims.security.http.AddEmployeeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@RestController
@CrossOrigin
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
    public ResponseEntity<Business> createBusiness(@AuthenticationPrincipal User authenticatedUser, @RequestBody Business body) {
        if (
                body.getName() == null ||
                body.getBusinessRegistrationDate() == null ||
                body.getTaxNumber() == null ||
                body.getHeadQuarters() == null
        ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Business business = Business.builder()
                .name(body.getName())
                .simsRegistrationDate(new Date())
                .businessRegistrationDate(body.getBusinessRegistrationDate())
                .taxNumber(body.getTaxNumber())
                .headQuarters(body.getHeadQuarters())
                .associates(new HashSet<>())
                .build();

        Optional<User> savedUserOptional = userRepository.findById(authenticatedUser.getId());
        if (savedUserOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        User savedUser = savedUserOptional.get();
        business.setOwner(savedUser);
        businessRepository.save(business);

        savedUser.getAssociatedBusinesses().add(business);
        userRepository.save(savedUser);

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
        userRepository.save(authenticatedUser);
        if(!authenticatedUser.getAssociatedBusinesses().isEmpty()){
            return ResponseEntity.ok(authenticatedUser.getAssociatedBusinesses());
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Business> getBusiness(@AuthenticationPrincipal User authenticatedUser, @PathVariable Integer id){
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if(optionalBusiness.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Business business = optionalBusiness.get();
        if(!business.getAssociates().contains(authenticatedUser)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(business);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBusiness(@AuthenticationPrincipal User authenticatedUser, @PathVariable Integer id) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if (optionalBusiness.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Business business = optionalBusiness.get();

        if (!authenticatedUser.equals(business.getOwner())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            for (User associate : business.getAssociates()) {
                associate.getAssociatedBusinesses().remove(business);
                userRepository.save(associate);
            }
            business.setOwner(null);
            business.getAssociates().clear();
            Optional<Set<Inventory>> optionalInventories = inventoryRepository.findAllByBusinessId(business.getId());
            if(optionalInventories.isPresent()){
                Set<Inventory> inventories = optionalInventories.get();
                for (Inventory inventory : inventories) {
                    inventoryRepository.delete(inventory);
                    inventoryRepository.flush();
                }
            }


            businessRepository.delete(business);
            businessRepository.flush();

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Business> updateBusiness(@AuthenticationPrincipal User authenticatedUser, @PathVariable Integer id, @RequestBody Business body){
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if(optionalBusiness.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Business business = optionalBusiness.get();
        if (!business.getOwner().equals(authenticatedUser)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        business.setName(body.getName());
        business.setHeadQuarters(body.getHeadQuarters());
        business.setBusinessRegistrationDate(body.getBusinessRegistrationDate());
        business.setTaxNumber(body.getTaxNumber());

        businessRepository.save(business);
        return ResponseEntity.ok(business);
    }
    @PutMapping("/{id}/addEmployee")
    public ResponseEntity<?> addEmployee(@AuthenticationPrincipal User authenticatedUser, @PathVariable Integer id, @RequestBody AddEmployeeRequest body){
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if(optionalBusiness.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Business business = optionalBusiness.get();
        //Only the business owner can add an employee
        if(!business.getOwner().equals(authenticatedUser)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<User> optionalEmployee = userRepository.findByEmail(body.getEmail());
        if (optionalEmployee.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User employee = optionalEmployee.get();

        employee.setHomeAddress(body.getHomeAddress());
        employee.setDateOfBirth(body.getDateOfBirth());
        employee.setPlaceOfBirth(body.getPlaceOfBirth());
        employee.setCitizenship(body.getCitizenship());
        employee.setIdentityCardNumber(body.getIdentityCardNumber());
        employee.setSocialSecurityNumber(body.getSocialSecurityNumber());
        employee.setPhoneNumber(body.getPhoneNumber());

        employee.getAssociatedBusinesses().add(business);
        userRepository.save(employee);
        business.getAssociates().add(employee);
        businessRepository.save(business);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{businessId}/removeEmployee/{employeeId}")
    public ResponseEntity<?> removeEmployee(@AuthenticationPrincipal User authenticatedUser, @PathVariable Integer businessId, @PathVariable Integer employeeId){
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if(optionalBusiness.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Business business = optionalBusiness.get();
        if(!business.getOwner().equals(authenticatedUser)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<User> optionalEmployee = userRepository.findById(employeeId);
        if (optionalEmployee.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User employee = optionalEmployee.get();

        employee.getAssociatedBusinesses().remove(business);
        userRepository.save(employee);
        business.getAssociates().remove(employee);
        businessRepository.save(business);
        return ResponseEntity.ok().build();
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
