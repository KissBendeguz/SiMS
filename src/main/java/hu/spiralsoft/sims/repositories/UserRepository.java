package hu.spiralsoft.sims.repositories;

import hu.spiralsoft.sims.entities.Business;
import hu.spiralsoft.sims.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<Business> findById(int id);
    Optional<User> findByEmail(String email);
}
