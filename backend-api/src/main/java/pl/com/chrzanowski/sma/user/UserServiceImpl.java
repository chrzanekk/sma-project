package pl.com.chrzanowski.sma.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.auth.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.response.UserInfoResponse;
import pl.com.chrzanowski.sma.auth.usertokens.UserTokenDTO;
import pl.com.chrzanowski.sma.auth.usertokens.UserTokenService;
import pl.com.chrzanowski.sma.enumeration.ERole;
import pl.com.chrzanowski.sma.role.Role;
import pl.com.chrzanowski.sma.role.RoleDTO;
import pl.com.chrzanowski.sma.role.RoleRepository;
import pl.com.chrzanowski.sma.role.RoleService;
import pl.com.chrzanowski.sma.security.SecurityUtils;
import pl.com.chrzanowski.sma.util.EmailUtil;

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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final UserTokenService userTokenService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           UserMapper userMapper,
                           RoleService roleService,
                           PasswordEncoder encoder, UserTokenService userTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

//TODO reimplement basic roles when register. this below is not best solution
        if (stringRoles == null || stringRoles.isEmpty()) {
            roleDTOSet.add(roleService.findByName(ERole.ROLE_USER));
        } else {
            stringRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        RoleDTO adminRole = roleService.findByName(ERole.ROLE_ADMIN);
                        roleDTOSet.add(adminRole);
                    }
                    case "mod" -> {
                        RoleDTO modeRole = roleService.findByName(ERole.ROLE_MODERATOR);
                        roleDTOSet.add(modeRole);
                    }
                    default -> {
                        RoleDTO userRole = roleService.findByName(ERole.ROLE_USER);
                        roleDTOSet.add(userRole);
                    }
                }
            });
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
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDTO)));
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        log.info("Update user {} to database", userDTO);
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDTO)));
    }

    @Override
    public List<UserDTO> findByFilter(UserFilter filter) {
        log.debug("Find all users by filter: {}", filter);
        Specification<User> specification = UserSpecification.create(filter);
        return userMapper.toDto(userRepository.findAll(specification));
    }

    @Override
    public Page<UserDTO> findByFilterAndPage(UserFilter filter, Pageable pageable) {
        log.debug("Find all users by filter and page: {}", filter);
        Specification<User> specification = UserSpecification.create(filter);
        return userRepository.findAll(specification, pageable).map(userMapper::toDto);
    }

    @Override
    public UserDTO findById(Long id) {
        log.debug("Find user by id: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        return userMapper.toDto(optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete user by id: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDTO> findAll() {
        log.info("Fetching all users. ");
        return userMapper.toDto(userRepository.findAll());
    }

    @Override
    public UserDTO getUser(String email) {
        log.info("Fetching user {} ", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
        return userMapper.toDto(user);
    }

    @Override
    public Boolean isUserExists(String userName) {
        log.debug("Request to check if userName exists in DB: {}", userName);
        return userRepository.existsByUsername(userName);
    }

    @Override
    public Boolean isEmailExists(String email) {
        log.debug("Request to check if email exists in DB: {}", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserInfoResponse getUserWithAuthorities() {
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User currentUser = userRepository.findByUsername(currentLogin).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<ERole> currentRoles = currentUser.getRoles().stream().map(Role::getName).toList();
        return new UserInfoResponse(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentRoles);
    }
}
