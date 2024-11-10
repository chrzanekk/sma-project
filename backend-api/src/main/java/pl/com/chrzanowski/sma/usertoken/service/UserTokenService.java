package pl.com.chrzanowski.sma.usertoken.service;

import pl.com.chrzanowski.sma.common.enumeration.TokenType;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;

public interface UserTokenService {

    String generateToken();

    UserTokenDTO saveToken(String token, UserDTO userDTO, TokenType tokenType);

    UserTokenDTO updateToken(UserTokenDTO userTokenDTO);

    UserTokenDTO getTokenData(String token);

    void deleteToken(Long id);

    void deleteTokenByUserId(Long userId);
}
