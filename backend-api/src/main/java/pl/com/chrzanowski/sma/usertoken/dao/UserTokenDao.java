package pl.com.chrzanowski.sma.usertoken.dao;

import pl.com.chrzanowski.sma.usertoken.model.UserToken;

import java.util.Optional;

public interface UserTokenDao {

    UserToken saveToken(UserToken userToken);

    UserToken updateToken(UserToken userToken);

    Optional<UserToken> findUserTokensByToken(String token);

    void deleteTokenById(Long id);

}
