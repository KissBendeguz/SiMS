package hu.spiralsoft.sims.controllers;


import hu.spiralsoft.sims.entities.Inventory;
import hu.spiralsoft.sims.entities.Product;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.InventoryRepository;
import hu.spiralsoft.sims.repositories.ProductRepository;
import hu.spiralsoft.sims.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/${application.api.ver}/product")
@RequiredArgsConstructor
public class ProductController {
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @PostMapping("/{inventoryId}")
    public ResponseEntity<Product> createProduct(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable Integer inventoryId,
            @RequestBody Product requestBody
    ) {

        if (requestBody != null) {

            Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);

            if (inventoryOptional.isPresent()) {
                Inventory inventory = inventoryOptional.get();

                Product product = Product.builder()
                        .name(requestBody.getName())
                        .quantity(requestBody.getQuantity())
                        .category(requestBody.getCategory())
                        .itemNumber(requestBody.getItemNumber())
                        .unit(requestBody.getUnit())
                        .addedToInventory(new Date())
                        .addedBy(authenticatedUser)
                        .inventory(inventory)
                        .dynProperties(requestBody.getDynProperties())
                        .build();

                productRepository.save(product);
                inventory.getProducts().add(product);
                inventoryRepository.save(inventory);
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.badRequest().build();
    }


    @DeleteMapping("/{inventoryId}/{productId}")
    public ResponseEntity<?> deleteProduct(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable Integer inventoryId,
            @PathVariable Integer productId
    ) {
        Optional<Product> productOptional = productRepository.findById(productId);
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);

        if (inventoryOptional.isPresent() && productOptional.isPresent()) {
            Product product = productOptional.get();
            Inventory inventory = inventoryOptional.get();

            inventory.getProducts().remove(product);
            inventoryRepository.save(inventory);
            productRepository.delete(product);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> modifyProduct(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable int id,
            @RequestBody Product requestBody
    ) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product existingProduct = productOptional.get();


                existingProduct.setName(requestBody.getName());
                existingProduct.setQuantity(requestBody.getQuantity());
                existingProduct.setCategory(requestBody.getCategory());
                existingProduct.setItemNumber(requestBody.getItemNumber());
                existingProduct.setUnit(requestBody.getUnit());
                //existingProduct.setDynProperties(requestBody.getDynProperties());

                productRepository.save(existingProduct);

                return ResponseEntity.ok(existingProduct);

        }

        return ResponseEntity.notFound().build();
    }
}
