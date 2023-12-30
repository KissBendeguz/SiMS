package hu.spiralsoft.sims.repositories;

import hu.spiralsoft.sims.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface InventoryRepository extends JpaRepository<Inventory,Integer> {
    Optional<Inventory> findById(int id);
    Optional<Inventory> findByBusinessId(int id);

    @Query("SELECT i FROM Inventory i WHERE i.business.id = :businessId")
    Optional<Set<Inventory>> findAllByBusinessId(@Param("businessId") Integer businessId);
}
