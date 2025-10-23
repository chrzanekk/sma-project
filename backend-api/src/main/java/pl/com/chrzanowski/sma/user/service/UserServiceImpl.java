package pl.com.chrzanowski.sma.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.request.UserEditPasswordChangeRequest;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.common.exception.UserNotFoundException;
import pl.com.chrzanowski.sma.common.security.SecurityUtils;
import pl.com.chrzanowski.sma.common.security.service.UserDetailsImpl;
import pl.com.chrzanowski.sma.common.util.EmailUtil;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.position.dto.PositionBaseDTO;
import pl.com.chrzanowski.sma.position.dto.PositionDTO;
import pl.com.chrzanowski.sma.position.mapper.PositionBaseMapper;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.service.RoleService;
import pl.com.chrzanowski.sma.user.dao.UserDao;
import pl.com.chrzanowski.sma.user.dto.AdminEditPasswordChangeRequest;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.mapper.UserDTOMapper;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.usertoken.service.UserTokenService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final static String USER_WITH_ID_NOT_FOUND = "user with id %d not found";

    private final UserDao userDao;
    private final UserDTOMapper userDTOMapper;
    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final PasswordEncoder encoder;
    private final UserTokenService userTokenService;
    private final SendEmailService sentEmailService;
    private final CompanyBaseMapper companyBaseMapper;
    private final PositionBaseMapper positionBaseMapper;

    public UserServiceImpl(UserDao userDao,
                           UserDTOMapper userDTOMapper,
                           RoleService roleService, RoleMapper roleMapper,
                           PasswordEncoder encoder, UserTokenService userTokenService, SendEmailService sentEmailService, CompanyBaseMapper companyBaseMapper, PositionBaseMapper positionBaseMapper) {
        this.userDao = userDao;
        this.userDTOMapper = userDTOMapper;
        this.roleService = roleService;
        this.roleMapper = roleMapper;
        this.userTokenService = userTokenService;
        this.encoder = encoder;
        this.sentEmailService = sentEmailService;
        this.companyBaseMapper = companyBaseMapper;
        this.positionBaseMapper = positionBaseMapper;
    }

    @Override
    @Transactional
    public UserDTO register(RegisterRequest request) {
        log.debug("Request to register new user: {}", request);
        EmailUtil.validateEmail(request.getEmail());
        UserDTO newUser = UserDTO.builder()
                .login(request.getLogin())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .position(request.getPosition())
                .enabled(false).locked(true).password(request.getPassword())
                .build();
        return save(newUser);
    }

    @Override
    @Transactional
    public MessageResponse confirm(String token) {
        log.debug("Confirm user registration by token: {}", token);
        UserTokenDTO userTokenDTO = userTokenService.getTokenData(token);
        if (userTokenDTO.getUseDate() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expireDate = userTokenDTO.getExpireDate();
        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired.");
        }

        UserTokenDTO confirmedDTO = userTokenService.updateToken(userTokenDTO);
        UserDTO user = getUserByEmail(confirmedDTO.getEmail());
        update(user.toBuilder().enabled(true).locked(lockUserWithoutAdminRole(user)).build());
        return new MessageResponse("Confirmed");
    }

    @Override
    @Transactional
    public UserDTO save(UserDTO userDTO) {
        log.debug("Saving new user {} to database", userDTO);

        Set<RoleDTO> stringRoles = userDTO.getRoles();
        Set<RoleDTO> roleDTOSet = new HashSet<>();
        if (stringRoles == null || stringRoles.isEmpty()) {
            List<UserDTO> userDTOList = findAll();
            if (userDTOList.isEmpty()) {
                RoleDTO adminRole = roleService.findByName(ERole.ROLE_ADMIN.getName());
                roleDTOSet.add(adminRole);
            } else {
                RoleDTO userRole = roleService.findByName(ERole.ROLE_USER.getName());
                roleDTOSet.add(userRole);
            }
        } else {
            roleDTOSet.addAll(stringRoles.stream().map(roleDTO -> roleService.findByName(roleDTO.getName())).collect(Collectors.toSet()));
        }

        UserDTO userDTOToSave = userDTO.toBuilder()
                .createdDatetime(Instant.now())
                .password(encoder.encode(userDTO.getPassword()))
                .roles(roleDTOSet)
                .build();

        User userEntity = userDTOMapper.toEntity(userDTOToSave);
        Set<Role> managedRoles = userEntity.getRoles().stream()
                .map(role -> roleMapper.toEntity(roleService.findByName(role.getName())))
                .collect(Collectors.toSet());
        userEntity.setRoles(managedRoles);

        return userDTOMapper.toDto(userDao.save(userEntity));
    }


    @Override
    @Transactional
    public UserDTO update(UserDTO userDTO) {
        log.debug("Update user {} to database", userDTO);
        UserDTO existingUserDTO = findById(userDTO.getId());
        UserDTO.UserDTOBuilder builder = existingUserDTO.toBuilder();

        if (existingUserDTO.getLogin() != null && userDTO.getLogin() != null && !existingUserDTO.getLogin().equals(userDTO.getLogin())) {
            if (isUserExists(userDTO.getLogin())) {
                throw new IllegalStateException("User with login %s already exists");
            }
            builder.login(userDTO.getLogin());
        }

        if (existingUserDTO.getEmail() != null && userDTO.getEmail() != null && !existingUserDTO.getEmail().equals(userDTO.getEmail())) {
            if (isUserExists(userDTO.getEmail())) {
                throw new IllegalStateException("User with email %s already exists");
            }
            builder.email(userDTO.getEmail());
        }
        builder.firstName(userDTO.getFirstName() != null ? userDTO.getFirstName() : existingUserDTO.getFirstName());
        builder.lastName(userDTO.getLastName() != null ? userDTO.getLastName() : existingUserDTO.getLastName());
        builder.position(userDTO.getPosition() != null ? userDTO.getPosition() : existingUserDTO.getPosition());
        builder.locked(userDTO.getLocked() != null ? userDTO.getLocked() : existingUserDTO.getLocked());
        builder.enabled(userDTO.getEnabled() != null ? userDTO.getEnabled() : existingUserDTO.getEnabled());
        builder.companies(userDTO.getCompanies() != null ? userDTO.getCompanies() : existingUserDTO.getCompanies());

        builder.lastModifiedDatetime(Instant.now());
        UserDTO updatedUserDTO = builder.build();

        return userDTOMapper.toDto(userDao.save(userDTOMapper.toEntity(updatedUserDTO)));
    }

    @Override
    @Transactional
    public void updateUserPassword(UserEditPasswordChangeRequest userEditPasswordChangeRequest) {
        log.debug("Update user password by id: {}", userEditPasswordChangeRequest.userId());
        UserDTO existingUserDTO = findById(userEditPasswordChangeRequest.userId());
        if (userEditPasswordChangeRequest.newPassword() == null || userEditPasswordChangeRequest.newPassword().isEmpty()) {
            throw new IllegalStateException("New password cannot be empty");
        }
        if (!encoder.matches(userEditPasswordChangeRequest.password(), existingUserDTO.getPassword())) {
            throw new IllegalStateException("Current password does not match");
        }
        UserDTO.UserDTOBuilder builder = existingUserDTO.toBuilder();
        String encodedPassword = encoder.encode(userEditPasswordChangeRequest.newPassword());
        builder.password(encodedPassword);
        builder.lastModifiedDatetime(Instant.now());
        UserDTO updatedUserDTO = builder.build();
        userDao.save(userDTOMapper.toEntity(updatedUserDTO));
    }


    @Override
    @Transactional
    public UserDTO updateUserRoles(Long userId, Set<RoleDTO> roles) {
        log.debug("Update user roles by id: {}", userId);
        UserDTO existingUserDTO = findById(userId);

        UserDTO.UserDTOBuilder builder = existingUserDTO.toBuilder();

        Set<RoleDTO> roleSet = roles.stream()
                .map(roleDTO -> roleService.findByName(roleDTO.getName()))
                .collect(Collectors.toSet());
        builder.roles(roleSet);
        builder.lastModifiedDatetime(Instant.now());
        UserDTO updatedUserDTO = builder.build();
        userDao.save(userDTOMapper.toEntity(updatedUserDTO));
        return updatedUserDTO;
    }


    @Override
    @Transactional
    public UserDTO findById(Long id) {
        log.debug("Find user by id: {}", id);
        Optional<User> optionalUser = userDao.findById(id);
        return userDTOMapper.toDto(optionalUser.orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, id))));
    }

    @Override
    @Transactional
    public List<UserDTO> findAll() {
        log.debug("Fetching all users. ");
        return userDTOMapper.toDtoList(userDao.findAll());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Delete user by id: {}", id);
        userTokenService.deleteTokenByUserId(id);
        sentEmailService.deleteEmailByUserId(id);
        userDao.deleteById(id);
    }

    @Override
    @Transactional
    public UserDTO getUserByEmail(String email) {
        log.debug("Fetching user by email: {} ", email);
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found", Map.of("email", email)));
        return userDTOMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDTO getUserByLogin(String login) {
        log.debug("Fetching user by login: {} ", login);
        User user = userDao.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found", Map.of("login", login)));
        return userDTOMapper.toDto(user);
    }

    @Override
    @Transactional
    public Boolean isUserExists(String userName) {
        log.debug("Request to check if userName exists in DB: {}", userName);
        return userDao.existsByLogin(userName);
    }

    @Override
    @Transactional
    public Boolean isEmailExists(String email) {
        log.debug("Request to check if email exists in DB: {}", email);
        return userDao.existsByEmail(email);
    }

    @Override
    @Transactional
    public UserInfoResponse getUserWithAuthorities() {
        log.debug("Get user with authorities.");
        User currentUser = getCurrentLoggedUser().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<String> currentRoles = currentUser.getRoles().stream().map(Role::getName).toList();
        List<CompanyBaseDTO> currentCompanies;
        PositionBaseDTO currentPosition = null;
        if (currentUser.getCompanies() != null && !currentUser.getCompanies().isEmpty()) {
            currentCompanies = currentUser.getCompanies().stream().map(companyBaseMapper::toDto).toList();
        } else {
            currentCompanies = Collections.emptyList();
        }
        if (currentUser.getPosition()!= null) {
            currentPosition = positionBaseMapper.toDto(currentUser.getPosition());
        }
        return new UserInfoResponse(
                currentUser.getId(),
                currentUser.getLogin(),
                currentUser.getEmail(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentPosition,
                currentRoles,
                currentCompanies);
    }

    @Override
    @Transactional
    public Optional<User> getCurrentLoggedUser() {
        log.debug("Get current logged user.");
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userDao.findByLogin(currentLogin);
    }

    @Override
    @Transactional
    public Long getCurrentUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        return null;
    }

    @Override
    @Transactional
    public void updateUserPasswordByAdmin(AdminEditPasswordChangeRequest adminEditPasswordChangeRequest) {
        log.debug("Update user password by admin for user: {}", adminEditPasswordChangeRequest.userId());
        UserDTO existingUserDTO = findById(adminEditPasswordChangeRequest.userId());
        if (adminEditPasswordChangeRequest.newPassword() == null || adminEditPasswordChangeRequest.newPassword().isEmpty()) {
            throw new IllegalStateException("New password cannot be empty");
        }
        UserDTO.UserDTOBuilder builder = existingUserDTO.toBuilder();
        String encodedPassword = encoder.encode(adminEditPasswordChangeRequest.newPassword());
        builder.password(encodedPassword);
        builder.lastModifiedDatetime(Instant.now());
        UserDTO updatedUserDTO = builder.build();
        userDao.save(userDTOMapper.toEntity(updatedUserDTO));
    }

    private boolean lockUserWithoutAdminRole(UserDTO userDTO) {
        if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
            return true;
        } else
            return userDTO.getRoles().stream().noneMatch(roleDTO -> roleDTO.getName().equals(ERole.ROLE_ADMIN.getName()));
    }
}
