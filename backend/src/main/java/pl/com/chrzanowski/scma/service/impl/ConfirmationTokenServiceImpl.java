package pl.com.chrzanowski.scma.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.domain.ConfirmationToken;
import pl.com.chrzanowski.scma.exception.ObjectNotFoundException;
import pl.com.chrzanowski.scma.repository.ConfirmationTokenRepository;
import pl.com.chrzanowski.scma.service.ConfirmationTokenService;
import pl.com.chrzanowski.scma.service.dto.ConfirmationTokenDTO;
import pl.com.chrzanowski.scma.service.dto.UserDTO;
import pl.com.chrzanowski.scma.service.mapper.ConfirmationTokenMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private static final int TOKEN_VALIDITY_TIME_IN_MINUTES = 30;
    private final Logger log = LoggerFactory.getLogger(ConfirmationTokenServiceImpl.class);

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ConfirmationTokenMapper confirmationTokenMapper;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository,
                                        ConfirmationTokenMapper confirmationTokenMapper) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.confirmationTokenMapper = confirmationTokenMapper;
    }

    @Override
    public ConfirmationTokenDTO saveToken(String token, UserDTO userDTO) {
        log.debug("Request to save token to confirm user registration: {}", token);
        ConfirmationTokenDTO confirmationTokenDTO = ConfirmationTokenDTO.builder()
                .confirmationToken(token)
                .createDate(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_TIME_IN_MINUTES))
                .userId(userDTO.getId())
                .email(userDTO.getEmail())
                .userName(userDTO.getUsername()).build();
        ConfirmationToken confirmationToken = confirmationTokenMapper.toEntity(confirmationTokenDTO);
        ConfirmationToken saved = confirmationTokenRepository.save(confirmationToken);
        return confirmationTokenMapper.toDto(saved);
    }

    @Override
    public ConfirmationTokenDTO updateToken(ConfirmationTokenDTO confirmationTokenDTO) {
        log.debug("Request to update confirmed token: {}", confirmationTokenDTO.getConfirmationToken());
        ConfirmationToken confirmed = confirmationTokenRepository.save(
                confirmationTokenMapper.toEntity(ConfirmationTokenDTO.builder(confirmationTokenDTO)
                        .confirmDate(LocalDateTime.now()).build()));
        return confirmationTokenMapper.toDto(confirmed);
    }

    @Override
    public ConfirmationTokenDTO getConfirmationToken(String token) {
        log.debug("Request to get confirmation token: {}", token);
        return confirmationTokenRepository.findByConfirmationToken(token).map(confirmationTokenMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundException("Token not found"));
    }

    @Override
    public void deleteConfirmationToken(Long id) {
        log.debug("Request to delete token after confirmation: {}", id);
        confirmationTokenRepository.deleteById(id);
    }

    @Override
    public String generateToken() {
        log.debug("Request to generate confirmation token");
        return UUID.randomUUID().toString();
    }
}
