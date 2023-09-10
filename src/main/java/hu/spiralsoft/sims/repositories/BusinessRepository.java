package hu.spiralsoft.sims.repositories;

import hu.spiralsoft.sims.entities.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business,Integer> {
    Optional<Business> findByName(String name);
}
