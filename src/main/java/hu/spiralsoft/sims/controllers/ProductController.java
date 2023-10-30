package hu.spiralsoft.sims.controllers;


import hu.spiralsoft.sims.entities.Product;
import hu.spiralsoft.sims.entities.User;
import hu.spiralsoft.sims.repositories.InventoryRepository;
import hu.spiralsoft.sims.repositories.ProductRepository;
import hu.spiralsoft.sims.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${application.api.ver}/product")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final InventoryRepository inventoryRepository;
    @Autowired
    private final ProductRepository productRepository;

    @PostMapping("")
    public ResponseEntity<Product> createProduct(@AuthenticationPrincipal User authenticatedUser,RequestBody Product ){
        throw new NotImplementedException();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal User authenticatedUser, @PathVariable int id){
        throw new NotImplementedException();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> modifyProduct(@AuthenticationPrincipal User authenticatedUser, @PathVariable int id,@RequestBody Product body){
        throw new NotImplementedException();
    }

}
