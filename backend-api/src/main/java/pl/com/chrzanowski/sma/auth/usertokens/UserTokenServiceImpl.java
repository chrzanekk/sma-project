package pl.com.chrzanowski.sma.auth.usertokens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.common.enumeration.TokenType;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;
import pl.com.chrzanowski.sma.user.UserDTO;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    private static final int TOKEN_VALIDITY_TIME_IN_MINUTES = 30;

    private final Logger log = LoggerFactory.getLogger(UserTokenServiceImpl.class);
    private final UserTokenRepository userTokenRepository;
    private final UserTokenMapper userTokenMapper;

    public UserTokenServiceImpl(UserTokenRepository userTokenRepository, UserTokenMapper userTokenMapper) {
        this.userTokenRepository = userTokenRepository;
        this.userTokenMapper = userTokenMapper;
    }

    @Override
    public String generateToken() {
        log.debug("Request to generate password reset token.");
        return UUID.randomUUID().toString();
    }

    @Override
    public UserTokenDTO saveToken(String token, UserDTO userDTO, TokenType tokenType) {
        log.debug("Request to save token: {}, {}", tokenType.name(), token);
        if (userDTO == null) {
            throw new ObjectNotFoundException("User must not be null");
        } else {
            UserTokenDTO userTokenDTO = UserTokenDTO.builder()
                    .token(token)
                    .createDate(LocalDateTime.now())
                    .expireDate(LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_TIME_IN_MINUTES))
                    .userId(userDTO.getId())
                    .email(userDTO.getEmail())
                    .userName(userDTO.getUsername())
                    .tokenType(tokenType)
                    .build();
            return userTokenMapper.toDto(userTokenRepository.save(userTokenMapper.toEntity(userTokenDTO)));
        }
    }

    @Override
    public UserTokenDTO updateToken(UserTokenDTO userTokenDTO) {
        log.debug("Request to update token: {}", userTokenDTO.getToken());
        UserToken userToken = userTokenRepository.save(userTokenMapper.toEntity(userTokenDTO.toBuilder()
                .useDate(LocalDateTime.now()).build()));
        return userTokenMapper.toDto(userToken);
    }

    @Override
    public UserTokenDTO getTokenData(String token) {
        log.debug("Request to get token data: {}", token);
        return userTokenRepository.findUserTokensByToken(token).map(userTokenMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundException("Token not found."));
    }

    @Override
    public void deleteConfirmationToken(Long id) {
        log.debug("Request to delete token: {}", id);
        userTokenRepository.deleteById(id);
    }
}
