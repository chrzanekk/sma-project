package pl.com.chrzanowski.sma.user.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;
import pl.com.chrzanowski.sma.user.service.filter.UserQuerySpec;

import java.util.List;
import java.util.Optional;

import static pl.com.chrzanowski.sma.role.model.QRole.role;
import static pl.com.chrzanowski.sma.user.model.QUser.user;

@Repository("userJPA")
public class UserJPADaoImpl implements UserDao {

    private final Logger log = LoggerFactory.getLogger(UserJPADaoImpl.class);

    private final UserRepository userRepository;
    private final UserQuerySpec userQuerySpec;

    public UserJPADaoImpl(UserRepository userRepository, UserQuerySpec userQuerySpec) {
        this.userRepository = userRepository;
        this.userQuerySpec = userQuerySpec;

    }

    @Override
    public User save(User user) {
        log.debug("DAO: Save user: {}", user.getLogin());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.debug("DAO: Find user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByLogin(String username) {
        log.debug("DAO: Find user by login: {}", username);
        return userRepository.findByLogin(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        log.debug("DAO: Check if user exists by email: {}", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByLogin(String login) {
        log.debug("DAO: Check if user exists by login: {}", login);
        return userRepository.existsByLogin(login);
    }

    @Override
    public Optional<User> findById(Long id) {
        log.debug("DAO: Find user by id: {}", id);
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        log.debug("DAO: Find all users");
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Find all users by specification with page: {}", specification);
        JPQLQuery<User> query = userQuerySpec.buildQuery(specification, pageable);

        long total = query.fetchCount();

        JPAQuery<User> jpaQuery = getPaginationQuery(query);

        List<User> content = jpaQuery.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        return new PageImpl<>(content, pageable, total);
    }

    private JPAQuery<User> getPaginationQuery(JPQLQuery<User> baseQuery) {
        JPAQuery<User> jpaQuery = (JPAQuery<User>) baseQuery;
        jpaQuery.leftJoin(user.roles, role).fetchJoin();
        return jpaQuery;
    }

    @Override
    public List<User> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find all users by specification {}", specification);
        JPQLQuery<User> query = userQuerySpec.buildQuery(specification, null);
        return query.fetch();
    }

    @Override
    public void deleteById(Long id) {
        log.debug("DAO: Delete user by id: {}", id);
        userRepository.deleteById(id);
    }
}
