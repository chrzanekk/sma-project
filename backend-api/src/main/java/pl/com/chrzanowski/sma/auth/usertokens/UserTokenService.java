package pl.com.chrzanowski.sma.auth.usertokens;

import pl.com.chrzanowski.sma.enumeration.TokenType;
import pl.com.chrzanowski.sma.user.UserDTO;

public interface UserTokenService {

    String generateToken();

    UserTokenDTO saveToken(String token, UserDTO userDTO, TokenType tokenType);

    UserTokenDTO updateToken(UserTokenDTO userTokenDTO);

    UserTokenDTO getTokenData(String token);

    void deleteConfirmationToken(Long id);

}
