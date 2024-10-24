package pl.com.chrzanowski.sma.usertoken.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.usertoken.model.UserToken;
import pl.com.chrzanowski.sma.usertoken.repository.UserTokenRepository;

import java.util.Optional;

@Repository("jpa")
public class UserTokenJPADaoImpl implements UserTokenDao {

    private final Logger log = LoggerFactory.getLogger(UserTokenJPADaoImpl.class);

    private final UserTokenRepository userTokenRepository;

    public UserTokenJPADaoImpl(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    @Override
    public UserToken saveToken(UserToken userToken) {
        log.debug("JPA DAO: save user token");
        return userTokenRepository.save(userToken);
    }

    @Override
    public UserToken updateToken(UserToken userToken) {
        log.debug("JPA DAO: update user token");
        return userTokenRepository.save(userToken);
    }

    @Override
    public Optional<UserToken> findUserTokensByToken(String token) {
        log.debug("JPA DAO: find user token by token");
        return userTokenRepository.findUserTokensByToken(token);
    }

    @Override
    public void deleteTokenById(Long id) {
        log.debug("JPA DAO: delete confirmation token");
        userTokenRepository.deleteById(id);
    }
}
