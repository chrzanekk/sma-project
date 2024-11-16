package pl.com.chrzanowski.sma.user.dao;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    Optional<User> findById(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByLogin(String login);

    List<User> findAll();

    Page<User> findAll(BooleanBuilder specification, Pageable pageable);

    List<User> findAll(BooleanBuilder specification);

    void deleteById(Long id);
}
