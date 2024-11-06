package pl.com.chrzanowski.sma.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.role.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    void deleteById(Long id);
}
