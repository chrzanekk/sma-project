package pl.com.chrzanowski.sma.user.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;
import pl.com.chrzanowski.sma.common.security.SecurityUtils;
import pl.com.chrzanowski.sma.common.util.EmailUtil;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.service.RoleService;
import pl.com.chrzanowski.sma.user.dao.UserDao;
import pl.com.chrzanowski.sma.user.dto.AdminEditPasswordChangeRequest;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.auth.dto.request.UserEditPasswordChangeRequest;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;
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

    private final static String USER_WITH_EMAIL_NOT_FOUND = "user with email %s not found";
    private final static String USER_WITH_ID_NOT_FOUND = "user with id %d not found";

    private final UserDao userDao;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final PasswordEncoder encoder;
    private final UserTokenService userTokenService;

    public UserServiceImpl(UserDao userDao,
                           UserMapper userMapper,
                           RoleService roleService, RoleMapper roleMapper,
                           PasswordEncoder encoder, UserTokenService userTokenService) {
        this.userDao = userDao;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.roleMapper = roleMapper;
        this.userTokenService = userTokenService;
        this.encoder = encoder;
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
        UserDTO user = getUser(confirmedDTO.getEmail());
        update(user.toBuilder().enabled(true).locked(true).build());
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
                RoleDTO adminRole = roleService.findByName(ERole.ROLE_ADMIN.getRoleName());
                roleDTOSet.add(adminRole);
            } else {
                RoleDTO userRole = roleService.findByName(ERole.ROLE_USER.getRoleName());
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

        User userEntity = userMapper.toEntity(userDTOToSave);
        Set<Role> managedRoles = userEntity.getRoles().stream()
                .map(role -> roleMapper.toEntity(roleService.findByName(role.getName())))
                .collect(Collectors.toSet());
        userEntity.setRoles(managedRoles);

        return userMapper.toDto(userDao.save(userEntity));
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

        builder.lastModifiedDatetime(Instant.now());
        UserDTO updatedUserDTO = builder.build();

        return userMapper.toDto(userDao.save(userMapper.toEntity(updatedUserDTO)));
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
        userDao.save(userMapper.toEntity(updatedUserDTO));
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
        userDao.save(userMapper.toEntity(updatedUserDTO));
        return updatedUserDTO;
    }


    @Override
    @Transactional
    public UserDTO findById(Long id) {
        log.debug("Find user by id: {}", id);
        Optional<User> optionalUser = userDao.findById(id);
        return userMapper.toDto(optionalUser.orElseThrow(() -> new ObjectNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, id))));
    }

    @Override
    @Transactional
    public List<UserDTO> findAll() {
        log.debug("Fetching all users. ");
        return userMapper.toDtoList(userDao.findAll());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Delete user by id: {}", id);
        userTokenService.deleteTokenByUserId(id);
        userDao.deleteById(id);
    }

    @Override
    @Transactional
    public UserDTO getUser(String email) {
        log.debug("Fetching user {} ", email);
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_WITH_EMAIL_NOT_FOUND, email)));
        return userMapper.toDto(user);
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
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User currentUser = userDao.findByLogin(currentLogin).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<String> currentRoles = currentUser.getRoles().stream().map(Role::getName).toList();
        return new UserInfoResponse(
                currentUser.getId(),
                currentUser.getLogin(),
                currentUser.getEmail(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getPosition(),
                currentRoles);
    }

    @Override
    public void updateUserPasswordByAdmin(AdminEditPasswordChangeRequest adminEditPasswordChangeRequest) {
        log.debug("Update user password by admin for user: {}",adminEditPasswordChangeRequest.userId());
        UserDTO existingUserDTO = findById(adminEditPasswordChangeRequest.userId());
        if (adminEditPasswordChangeRequest.newPassword() == null || adminEditPasswordChangeRequest.newPassword().isEmpty()) {
            throw new IllegalStateException("New password cannot be empty");
        }
        UserDTO.UserDTOBuilder builder = existingUserDTO.toBuilder();
        String encodedPassword = encoder.encode(adminEditPasswordChangeRequest.newPassword());
        builder.password(encodedPassword);
        builder.lastModifiedDatetime(Instant.now());
        UserDTO updatedUserDTO = builder.build();
        userDao.save(userMapper.toEntity(updatedUserDTO));
    }
}
