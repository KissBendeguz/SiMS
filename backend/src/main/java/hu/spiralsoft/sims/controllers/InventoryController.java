package hu.spiralsoft.sims.controllers;

import hu.spiralsoft.sims.entities.Business;
import hu.spiralsoft.sims.entities.Inventory;
import hu.spiralsoft.sims.entities.Product;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.BusinessRepository;
import hu.spiralsoft.sims.repositories.InventoryRepository;
import hu.spiralsoft.sims.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/${application.api.ver}/inventory")
@RequiredArgsConstructor
public class InventoryController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @PostMapping("/{id}")
    public ResponseEntity<Inventory> createInventory(@AuthenticationPrincipal User authenticatedUser, @PathVariable Integer id, @RequestBody Inventory body) {
        if (body == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Business> oBusiness = businessRepository.findById(id);
        if (oBusiness.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Business business = oBusiness.get();
        if(!authenticatedUser.equals(business.getOwner())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Inventory inventory = Inventory.builder()
                .name(body.getName())
                .address(body.getAddress())
                .managerName(body.getManagerName())
                .managerPhone(body.getManagerPhone())
                .managerEmail(body.getManagerEmail())
                .products(new HashSet<>())
                .business(business)
                .build();
        inventoryRepository.save(inventory);

        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@AuthenticationPrincipal User authenticatedUser,@PathVariable Integer id){
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);

        if (inventoryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Inventory inventory = inventoryOptional.get();
        if(!authenticatedUser.equals(inventory.getBusiness().getOwner())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        inventoryRepository.delete(inventory);
        inventoryRepository.flush();

        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventory(@AuthenticationPrincipal User authenticatedUser, @PathVariable int id) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);

        if (inventoryOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Inventory inventory = inventoryOptional.get();
        return ResponseEntity.ok(inventory);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Inventory> modifyInventory(@AuthenticationPrincipal User authenticatedUser, @PathVariable int id, @RequestBody Inventory body) {
        Optional<Inventory> oInventory = inventoryRepository.findById(id);

        if (oInventory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Inventory existingInventory = oInventory.get();
        if (!existingInventory.getBusiness().getOwner().equals(authenticatedUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        existingInventory.setName(body.getName());
        existingInventory.setAddress(body.getAddress());
        existingInventory.setManagerEmail(body.getManagerEmail());
        existingInventory.setManagerName(body.getManagerName());
        existingInventory.setManagerPhone(body.getManagerPhone());

        inventoryRepository.save(existingInventory);
        return ResponseEntity.ok(existingInventory);
    }


    @GetMapping("/{id}/products")
    public ResponseEntity<Set<Product>> getAllProducts(@AuthenticationPrincipal User authenticatedUser, @PathVariable int id) {
        Optional<Inventory> oInventory = inventoryRepository.findById(id);

        if (oInventory.isPresent()) {
            Inventory inventory = oInventory.get();
            if (inventory.getBusiness().getOwner().equals(authenticatedUser)) {
                return ResponseEntity.ok(inventory.getProducts());
            }
        }

        return ResponseEntity.notFound().build();
    }
}
