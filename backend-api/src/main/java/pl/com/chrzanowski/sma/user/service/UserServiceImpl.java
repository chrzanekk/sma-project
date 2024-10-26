package pl.com.chrzanowski.sma.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.common.security.SecurityUtils;
import pl.com.chrzanowski.sma.common.util.EmailUtil;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.service.RoleService;
import pl.com.chrzanowski.sma.user.dao.UserDao;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.filter.UserFilter;
import pl.com.chrzanowski.sma.user.service.filter.UserSpecification;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.usertoken.service.UserTokenService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final static String USER_NOT_FOUND = "user with email %s not found";

    private final UserDao userDao;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final UserTokenService userTokenService;

    public UserServiceImpl(UserDao userDao,
                           UserMapper userMapper,
                           RoleService roleService,
                           PasswordEncoder encoder, UserTokenService userTokenService) {
        this.userDao = userDao;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.userTokenService = userTokenService;
        this.encoder = encoder;
    }

    @Override
    public UserDTO register(RegisterRequest request) {
        log.debug("Request to register new user: {}", request);
        EmailUtil.validateEmail(request.getEmail());
        Set<String> stringRoles = request.getRole();
        Set<RoleDTO> roleDTOSet = new HashSet<>();
        if (stringRoles == null || stringRoles.isEmpty()) {
            List<UserDTO> userDTOList = findAll();
            if (userDTOList.isEmpty()) {
                RoleDTO adminRole = roleService.findByName(ERole.ROLE_ADMIN);
                roleDTOSet.add(adminRole);
            } else {
                roleDTOSet.add(roleService.findByName(ERole.ROLE_USER));
            }
        }
        UserDTO newUser = UserDTO.builder().username(request.getUsername()).email(request.getEmail())
                .roles(roleDTOSet).enabled(false).locked(false).password(encoder.encode(request.getPassword()))
                .build();
        return save(newUser);
    }

    @Override
    @Transactional
    public String confirm(String token) {
        UserTokenDTO userTokenDTO = userTokenService.getTokenData(token);
        if (userTokenDTO.getUseDate() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expireDate = userTokenDTO.getExpireDate();
        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired.");
        }

        UserTokenDTO confirmedDTO = userTokenService.updateToken(userTokenDTO);
        UserDTO user = getUser(confirmedDTO.getEmail());
        update(user.toBuilder().enabled(true).locked(false).build());
        return "Confirmed at" + confirmedDTO.getUseDate().toString();
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        log.info("Saving new user {} to database", userDTO);
        UserDTO userDTOToSave = userDTO.toBuilder().createdDatetime(Instant.now()).build();
        return userMapper.toDto(userDao.save(userMapper.toEntity(userDTOToSave)));
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        log.info("Update user {} to database", userDTO);
        UserDTO userDTOToUpdate = userDTO.toBuilder().lastModifiedDatetime(Instant.now()).build();
        return userMapper.toDto(userDao.save(userMapper.toEntity(userDTOToUpdate)));
    }

    @Override
    public List<UserDTO> findByFilter(UserFilter filter) {
        log.debug("Find all users by filter: {}", filter);
        Specification<User> specification = UserSpecification.create(filter);
        return userMapper.toDtoList(userDao.findAll(specification));
    }

    @Override
    public Page<UserDTO> findByFilterAndPage(UserFilter filter, Pageable pageable) {
        log.debug("Find all users by filter and page: {}", filter);
        Specification<User> specification = UserSpecification.create(filter);
        return userDao.findAll(specification, pageable).map(userMapper::toDto);
    }

    @Override
    public UserDTO findById(Long id) {
        log.debug("Find user by id: {}", id);
        Optional<User> optionalUser = userDao.findById(id);
        return userMapper.toDto(optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete user by id: {}", id);
        userDao.deleteById(id);
    }

    @Override
    public List<UserDTO> findAll() {
        log.info("Fetching all users. ");
        return userMapper.toDtoList(userDao.findAll());
    }

    @Override
    public UserDTO getUser(String email) {
        log.info("Fetching user {} ", email);
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
        return userMapper.toDto(user);
    }

    @Override
    public Boolean isUserExists(String userName) {
        log.debug("Request to check if userName exists in DB: {}", userName);
        return userDao.existsByUsername(userName);
    }

    @Override
    public Boolean isEmailExists(String email) {
        log.debug("Request to check if email exists in DB: {}", email);
        return userDao.existsByEmail(email);
    }

    @Override
    public UserInfoResponse getUserWithAuthorities() {
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User currentUser = userDao.findByUsername(currentLogin).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<ERole> currentRoles = currentUser.getRoles().stream().map(Role::getName).toList();
        return new UserInfoResponse(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentRoles);
    }
}
