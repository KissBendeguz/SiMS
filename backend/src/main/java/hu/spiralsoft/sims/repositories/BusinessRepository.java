package hu.spiralsoft.sims.repositories;

import hu.spiralsoft.sims.entities.Business;
import hu.spiralsoft.sims.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface BusinessRepository extends JpaRepository<Business,Integer> {
    Optional<Business> findById(Integer id);
    Optional<Business> findByName(String name);
    Optional<Set<Business>> findByOwner(User user);
}
