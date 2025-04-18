package pl.com.chrzanowski.sma.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.user.model.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>, QuerydslPredicateExecutor<User> {

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"roles", "companies"})
    Optional<User> findByLogin(String login);

    Boolean existsByEmail(String email);

    Boolean existsByLogin(String login);
}
