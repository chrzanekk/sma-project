package pl.com.chrzanowski.sma.user.dao;

import pl.com.chrzanowski.sma.common.dao.BaseCrudDao;
import pl.com.chrzanowski.sma.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends BaseCrudDao<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    Boolean existsByEmail(String email);

    Boolean existsByLogin(String login);

    List<User> findAll();
}
