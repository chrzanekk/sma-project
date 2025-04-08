package pl.com.chrzanowski.sma.unitTests.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.request.UserEditPasswordChangeRequest;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.common.exception.UserNotFoundException;
import pl.com.chrzanowski.sma.common.security.SecurityUtils;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.service.RoleService;
import pl.com.chrzanowski.sma.user.dao.UserDao;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.mapper.UserDTOMapper;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserServiceImpl;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.usertoken.service.UserTokenService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private UserDTOMapper userDTOMapper;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserTokenService userTokenService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private RegisterRequest registerRequest;
    private AutoCloseable autoCloseable;
    private User user;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userDTO = UserDTO.builder()
                .login("testUser")
                .email("test@example.com")
                .password("encodedPassword")
                .build();
        registerRequest = new RegisterRequest();
        registerRequest.setLogin("testUser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        roleUser = Role.builder().id(1L).name(ERole.ROLE_USER.getName()).build();
        user = User.builder().id(1L).login("testUser").email("test@example.com").roles(Set.of(roleUser)).companies(Collections.emptySet()).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testRegisterUserWithDefaultRole() {
        RoleDTO roleUser = RoleDTO.builder().id(1L).name(ERole.ROLE_USER.getName()).build();
        when(roleService.findByName(ERole.ROLE_USER.getName())).thenReturn(roleUser);
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(userDTOMapper.toEntity(any(UserDTO.class))).thenReturn(new User());
        when(userDao.save(any(User.class))).thenReturn(new User());
        when(userDTOMapper.toDto(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.register(registerRequest);

        assertEquals("testUser", result.getLogin());
        assertEquals("test@example.com", result.getEmail());
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void testConfirmTokenSuccess() {
        UserTokenDTO tokenDTO = UserTokenDTO.builder()
                .email("test@example.com")
                .expireDate(LocalDateTime.now().plusDays(1))
                .build();
        UserTokenDTO confirmTokenDTO = UserTokenDTO.builder()
                .email("test@example.com")
                .expireDate(LocalDateTime.now().plusDays(1))
                .useDate(LocalDateTime.now())
                .build();
        userDTO = userDTO.toBuilder().id(1L).build();
        when(userTokenService.getTokenData(anyString())).thenReturn(tokenDTO);
        when(userTokenService.updateToken(any(UserTokenDTO.class))).thenReturn(confirmTokenDTO);
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userDTOMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userDao.save(any(User.class))).thenReturn(user);
        when(userDTOMapper.toDto(any(User.class))).thenReturn(userDTO);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        MessageResponse result = userService.confirm("token123");

        assertNotNull(result);
        verify(userTokenService, times(1)).getTokenData("token123");
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void testConfirmTokenAlreadyConfirmed() {
        UserTokenDTO tokenDTO = UserTokenDTO.builder()
                .email("test@example.com")
                .expireDate(LocalDateTime.now().plusDays(1))
                .useDate(LocalDateTime.now())
                .build();

        when(userTokenService.getTokenData(anyString())).thenReturn(tokenDTO);
        when(userTokenService.updateToken(any(UserTokenDTO.class))).thenReturn(tokenDTO);
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userDTOMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userDao.save(any(User.class))).thenReturn(user);
        when(userDTOMapper.toDto(any(User.class))).thenReturn(userDTO);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.confirm("token123"));

        assertEquals("Email already confirmed", exception.getMessage());

        verify(userTokenService, times(1)).getTokenData("token123");
        verify(userTokenService, times(0)).updateToken(any(UserTokenDTO.class));
        verify(userDao, times(0)).save(any(User.class));
    }

    @Test
    void testConfirmTokenWhenTokenExpired() {
        UserTokenDTO tokenDTO = UserTokenDTO.builder()
                .email("test@example.com")
                .expireDate(LocalDateTime.now().minusDays(1))
                .build();

        when(userTokenService.getTokenData(anyString())).thenReturn(tokenDTO);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.confirm("token123"));

        assertEquals("Token expired.", exception.getMessage());

        verify(userTokenService, times(1)).getTokenData("token123");
        verify(userTokenService, times(0)).updateToken(any(UserTokenDTO.class));
        verify(userDao, times(0)).save(any(User.class));
    }

    @Test
    void testFindById_UserExists() {
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(userDTOMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.findById(1L);

        assertNotNull(result);
        assertEquals("testUser", result.getLogin());
    }

    @Test
    void testFindById_UserNotFound() {
        when(userDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testGetUser_UserByEmailExists() {
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userDTOMapper.toDto(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("testUser", result.getLogin());
        verify(userDao, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testGetUser_UserByEmailNotFound() {
        when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("nonexistent@example.com"));
    }

    @Test
    void testIsEmailExists() {
        when(userDao.existsByEmail("test@example.com")).thenReturn(true);

        Boolean result = userService.isEmailExists("test@example.com");

        assertTrue(result);
    }

    @Test
    void testIsUserExists() {
        when(userDao.existsByLogin("testUser")).thenReturn(true);

        Boolean result = userService.isUserExists("testUser");

        assertTrue(result);
    }

    @Test
    void deleteUserById() {
        Long id = 1L;

        userService.delete(id);

        verify(userDao, times(1)).deleteById(id);
    }

    @Test
    void findAllUsers() {

        userService.findAll();

        verify(userDao, times(1)).findAll();
    }

    @Test
    void saveNewUser() {
        when(userDTOMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(roleMapper.toEntity(any(RoleDTO.class))).thenReturn(roleUser);

        userService.save(userDTO);

        verify(userDao, times(1)).save(user);
    }

    @Test
    void updateUserWithSuccess() {
        userDTO = userDTO.toBuilder().id(1L).build();
        when(userDTOMapper.toDto(any(User.class))).thenReturn(userDTO);
        when(userDTOMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(userDao.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.update(userDTO);

        verify(userDao, times(1)).save(user);

        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getLogin(), result.getLogin());
        assertEquals(userDTO.getEmail(), result.getEmail());
    }

    @Test
    void testGetUserByEmailWithAuthoritiesSuccess() {

        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of("testUser"));
            when(userDao.findByLogin("testUser")).thenReturn(Optional.of(user));

            UserInfoResponse result = userService.getUserWithAuthorities();

            assertNotNull(result);
            assertEquals("testUser", result.login());
            assertEquals("test@example.com", result.email());
            assertEquals(1L, result.id());
            assertEquals(List.of(ERole.ROLE_USER.getName()), result.roles());

            securityUtilsMockedStatic.verify(SecurityUtils::getCurrentUserLogin, times(1));
        }

        verify(userDao, times(1)).findByLogin("testUser");
    }

    @Test
    void testGetUserWithAuthoritiesUserByEmailNotFound() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of("nonexistentUser"));
            when(userDao.findByLogin("nonexistentUser")).thenReturn(Optional.empty());

            assertThrows(UsernameNotFoundException.class, () -> userService.getUserWithAuthorities());

            securityUtilsMockedStatic.verify(SecurityUtils::getCurrentUserLogin, times(1));
        }
        verify(userDao, times(1)).findByLogin("nonexistentUser");
    }

    @Test
    void testUpdateUserPassword_Success() {
        UserEditPasswordChangeRequest passwordChangeRequest = new UserEditPasswordChangeRequest(1L, "password", "newPassword");
        userDTO = userDTO.toBuilder().id(1L).password("encodedCurrentPassword").build();

        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(userDTOMapper.toDto(user)).thenReturn(userDTO);
        when(encoder.matches("password", userDTO.getPassword())).thenReturn(true);
        when(encoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userDTOMapper.toEntity(any(UserDTO.class))).thenReturn(user);

        assertDoesNotThrow(() -> userService.updateUserPassword(passwordChangeRequest));

        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserPassword_IncorrectCurrentPassword() {
        UserEditPasswordChangeRequest passwordChangeRequest = new UserEditPasswordChangeRequest(1L, "wrongCurrentPassword", "newPassword");
        userDTO = userDTO.toBuilder().id(1L).password("encodedCurrentPassword").build();

        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(userDTOMapper.toDto(user)).thenReturn(userDTO);
        when(encoder.matches("wrongCurrentPassword", userDTO.getPassword())).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.updateUserPassword(passwordChangeRequest));
        assertEquals("Current password does not match", exception.getMessage());

        verify(userDao, times(0)).save(any(User.class));
    }

    @Test
    void testUpdateUserPassword_EmptyNewPassword() {
        UserEditPasswordChangeRequest passwordChangeRequest = new UserEditPasswordChangeRequest(1L, "password", "");

        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(userDTOMapper.toDto(user)).thenReturn(userDTO);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.updateUserPassword(passwordChangeRequest));
        assertEquals("New password cannot be empty", exception.getMessage());

        verify(userDao, times(0)).save(any(User.class));
    }

    @Test
    void testUpdateUserRoles_Success() {
        Set<RoleDTO> newRoles = Set.of(RoleDTO.builder().id(1L).name(ERole.ROLE_USER.getName()).build(), RoleDTO.builder().id(2L).name(ERole.ROLE_ADMIN.getName()).build());
        userDTO = userDTO.toBuilder().id(1L).roles(newRoles).build();

        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(userDTOMapper.toDto(user)).thenReturn(userDTO);
        when(userDTOMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(roleService.findByName(ERole.ROLE_USER.getName())).thenReturn(RoleDTO.builder().id(1L).name(ERole.ROLE_USER.getName()).build());
        when(roleService.findByName(ERole.ROLE_ADMIN.getName())).thenReturn(RoleDTO.builder().id(2L).name(ERole.ROLE_ADMIN.getName()).build());

        UserDTO updatedUser = userService.updateUserRoles(1L, newRoles);

        assertNotNull(updatedUser);
        assertEquals(2, updatedUser.getRoles().size());
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserRoles_InvalidRole() {
        Set<RoleDTO> newRoles = Set.of(RoleDTO.builder().id(1L).name("ROLE_NON_EXISTING").build());
        userDTO = userDTO.toBuilder().id(1L).roles(newRoles).build();

        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(userDTOMapper.toDto(user)).thenReturn(userDTO);
        when(roleService.findByName("ROLE_NON_EXISTING")).thenThrow(new IllegalArgumentException("Role not found"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUserRoles(1L, newRoles));
        assertEquals("Role not found", exception.getMessage());

        verify(userDao, times(0)).save(any(User.class));
    }

    @Test
    void testUpdateUserRoles_UserNotFound() {
        Set<RoleDTO> newRoles = Set.of(RoleDTO.builder().id(1L).name(ERole.ROLE_USER.getName()).build());

        when(userDao.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.updateUserRoles(1L, newRoles));
        assertEquals(String.format("user with id %d not found", 1L), exception.getMessage());

        verify(userDao, times(0)).save(any(User.class));
    }


}
