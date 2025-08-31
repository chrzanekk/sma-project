package pl.com.chrzanowski.sma.usertoken.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.enumeration.TokenType;
import pl.com.chrzanowski.sma.common.exception.TokenException;
import pl.com.chrzanowski.sma.common.exception.UserNotFoundException;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.usertoken.dao.UserTokenDao;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.usertoken.mapper.UserTokenMapper;
import pl.com.chrzanowski.sma.usertoken.model.UserToken;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class UserTokenServiceImpl implements UserTokenService {

    @Value("${jwt.tokenValidityTimeInMinutes}")
    private int TOKEN_VALIDITY_TIME_IN_MINUTES;

    private final Logger log = LoggerFactory.getLogger(UserTokenServiceImpl.class);
    private final UserTokenDao userTokenDao;
    private final UserTokenMapper userTokenMapper;

    public UserTokenServiceImpl(UserTokenDao userTokenDao, UserTokenMapper userTokenMapper) {
        this.userTokenDao = userTokenDao;
        this.userTokenMapper = userTokenMapper;
    }

    @Override
    @Transactional
    public String generateToken() {
        log.debug("Request to generate password reset token.");
        return UUID.randomUUID().toString();
    }

    @Override
    @Transactional
    public UserTokenDTO saveToken(String token, UserDTO userDTO, TokenType tokenType) {
        log.debug("Request to save token: {}, {}", tokenType.name(), token);
        if (userDTO == null) {
            throw new UserNotFoundException("User must not be null");
        } else if (token == null || token.isEmpty()) {
            throw new TokenException("Token must not be null or empty");
        } else {
            UserTokenDTO userTokenDTO = UserTokenDTO.builder()
                    .token(token)
                    .createDate(LocalDateTime.now())
                    .expireDate(LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_TIME_IN_MINUTES))
                    .userId(userDTO.getId())
                    .email(userDTO.getEmail())
                    .login(userDTO.getLogin())
                    .tokenType(tokenType)
                    .build();
            return userTokenMapper.toDto(userTokenDao.saveToken(userTokenMapper.toEntity(userTokenDTO)));
        }
    }

    @Override
    @Transactional
    public UserTokenDTO updateToken(UserTokenDTO userTokenDTO) {
        log.debug("Request to update token: {}", userTokenDTO.getToken());
        UserToken userToken = userTokenDao.updateToken(userTokenMapper.toEntity(userTokenDTO.toBuilder()
                .useDate(LocalDateTime.now()).build()));
        return userTokenMapper.toDto(userToken);
    }

    @Override
    @Transactional
    public UserTokenDTO getTokenData(String token) {
        log.debug("Request to get token data: {}", token);
        return userTokenDao.findUserTokenByToken(token).map(userTokenMapper::toDto)
                .orElseThrow(() -> new TokenException("Token not found."));
    }

    @Override
    @Transactional
    public void deleteToken(Long id) {
        log.debug("Request to delete token: {}", id);
        userTokenDao.deleteTokenById(id);
    }

    @Override
    @Transactional
    public void deleteTokenByUserId(Long userId) {
        log.debug("Request to delete token by userId: {}", userId);
        userTokenDao.deleteTokensByUserId(userId);
    }
}
