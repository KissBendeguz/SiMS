package hu.spiralsoft.sims.repositories;

import hu.spiralsoft.sims.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface inventoryRepository extends JpaRepository<Inventory,Integer> {
    Optional<Inventory> findById(int id);
}
