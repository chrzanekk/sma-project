package pl.com.chrzanowski.sma.user.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.com.chrzanowski.sma.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    List<User> findAll();

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    List<User> findAll(Specification<User> specification);

    void deleteById(Long id);
}
