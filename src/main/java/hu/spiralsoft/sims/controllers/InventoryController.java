package hu.spiralsoft.sims.controllers;

import hu.spiralsoft.sims.entities.Inventory;
import hu.spiralsoft.sims.entities.Product;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.BusinessRepository;
import hu.spiralsoft.sims.repositories.InventoryRepository;
import hu.spiralsoft.sims.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/${application.api.ver}/inventory")
@RequiredArgsConstructor
public class InventoryController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventory(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable int id
    ) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);

        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            if (inventory.getBusiness().getOwner().equals(authenticatedUser)) {
                return ResponseEntity.ok(inventory);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Inventory> modifyInventory(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable int id,
            @RequestBody Inventory body
    ) {
        Optional<Inventory> oInventory = inventoryRepository.findById(id);

        if (oInventory.isPresent()) {
            Inventory existingInventory = oInventory.get();
            if (existingInventory.getBusiness().getOwner().equals(authenticatedUser)) {

                existingInventory.setName(body.getName());

                inventoryRepository.save(existingInventory);

                return ResponseEntity.ok(existingInventory);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Set<Product>> getAllProducts(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable int id
    ) {
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
