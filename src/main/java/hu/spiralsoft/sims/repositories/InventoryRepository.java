package hu.spiralsoft.sims.repositories;

import hu.spiralsoft.sims.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface InventoryRepository extends JpaRepository<Inventory,Integer> {
    Optional<Inventory> findById(int id);
    Optional<Inventory> findByBusinessId(int id);
    Optional<Set<Inventory>> findAllByBusinessId(Integer id);
}
