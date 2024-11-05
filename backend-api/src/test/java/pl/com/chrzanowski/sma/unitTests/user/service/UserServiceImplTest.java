package pl.com.chrzanowski.sma.unitTests.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;
import pl.com.chrzanowski.sma.user.dao.UserDao;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserServiceImpl;
import pl.com.chrzanowski.sma.user.service.filter.UserFilter;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.usertoken.service.UserTokenService;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.common.security.SecurityUtils;
import pl.com.chrzanowski.sma.role.service.RoleService;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    private UserMapper userMapper;

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
                .username("testUser")
                .email("test@example.com")
                .password("encodedPassword")
                .build();
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        roleUser = Role.builder().id(1L).name(ERole.ROLE_USER).build();
        user = User.builder().id(1L).username("testUser").email("test@example.com").roles(Set.of(roleUser)).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testRegisterUserWithDefaultRole() {
        RoleDTO roleUser = RoleDTO.builder().id(1L).name(ERole.ROLE_USER).build();
        when(roleService.findByName(ERole.ROLE_USER)).thenReturn(roleUser);
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(new User());
        when(userDao.save(any(User.class))).thenReturn(new User());
        when(userMapper.toDto(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.register(registerRequest);

        assertEquals("testUser", result.getUsername());
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
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userDao.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDTO);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        String result = userService.confirm("token123");

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
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userDao.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDTO);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.confirm("token123");
        });

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

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.confirm("token123");
        });

        assertEquals("Token expired.", exception.getMessage());

        verify(userTokenService, times(1)).getTokenData("token123");
        verify(userTokenService, times(0)).updateToken(any(UserTokenDTO.class));
        verify(userDao, times(0)).save(any(User.class));
    }

    @Test
    void testFindById_UserExists() {
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.findById(1L);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void testFindById_UserNotFound() {
        when(userDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testFindByFilter() {
        UserFilter filter = new UserFilter()
                .setEmailStartsWith("test")
                .setEnabled(true);
        when(userDao.findAll(any(Specification.class))).thenReturn(Arrays.asList(user));
        when(userMapper.toDtoList(anyList())).thenReturn(Arrays.asList(userDTO));


        List<UserDTO> result = userService.findByFilter(filter);

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());

        verify(userDao, times(1)).findAll(any(Specification.class));

        verify(userMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testGetUser_UserExists() {
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.getUser("test@example.com");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userDao, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testGetUser_UserNotFound() {
        when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUser("nonexistent@example.com"));
    }

    @Test
    void testIsEmailExists() {
        when(userDao.existsByEmail("test@example.com")).thenReturn(true);

        Boolean result = userService.isEmailExists("test@example.com");

        assertTrue(result);
    }

    @Test
    void testIsUserExists() {
        when(userDao.existsByUsername("testUser")).thenReturn(true);

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
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);

        userService.save(userDTO);

        verify(userDao,times(1)).save(user);
    }

    @Test
    void updateUserWithSuccess() {
        userDTO = userDTO.toBuilder().id(1L).build();
        when(userMapper.toDto(any(User.class))).thenReturn(userDTO);
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(userDao.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.update(userDTO);

        verify(userDao,times(1)).save(user);

        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getEmail(), result.getEmail());
    }

    @Test
    void testGetUserWithAuthoritiesSuccess() {

        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of("testUser"));
            when(userDao.findByUsername("testUser")).thenReturn(Optional.of(user));

            UserInfoResponse result = userService.getUserWithAuthorities();

            assertNotNull(result);
            assertEquals("testUser", result.username());
            assertEquals("test@example.com", result.email());
            assertEquals(1L, result.id());
            assertEquals(List.of(ERole.ROLE_USER), result.roles());

            securityUtilsMockedStatic.verify(SecurityUtils::getCurrentUserLogin, times(1));
        }

        verify(userDao, times(1)).findByUsername("testUser");
    }

    @Test
    void testGetUserWithAuthoritiesUserNotFound() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of("nonexistentUser"));
            when(userDao.findByUsername("nonexistentUser")).thenReturn(Optional.empty());

            assertThrows(UsernameNotFoundException.class, () -> userService.getUserWithAuthorities());

            securityUtilsMockedStatic.verify(SecurityUtils::getCurrentUserLogin, times(1));
        }
        verify(userDao, times(1)).findByUsername("nonexistentUser");
    }

    @Test
    void testFindByFilterAndPage_Success() {
        UserFilter filter = new UserFilter()
                .setEmailStartsWith("test")
                .setEnabled(true);
        Pageable pageable = PageRequest.of(0, 10);
        User user = User.builder().id(1L).username("testUser").email("test@example.com").build();
        UserDTO userDTO = UserDTO.builder().id(1L).username("testUser").email("test@example.com").build();

        when(userDao.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        Page<UserDTO> result = userService.findByFilterAndPage(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testUser", result.getContent().get(0).getUsername());

        verify(userDao, times(1)).findAll(any(Specification.class), eq(pageable));
        verify(userMapper, times(1)).toDto(any(User.class));
    }

    @Test
    void testFindByFilterAndPage_NoResults() {
        UserFilter filter = new UserFilter()
                .setEmailStartsWith("nonexistent")
                .setEnabled(true);
        Pageable pageable = PageRequest.of(0, 10);
        when(userDao.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(Page.empty(pageable));


        Page<UserDTO> result = userService.findByFilterAndPage(filter, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Result should be empty");

        verify(userDao, times(1)).findAll(any(Specification.class), eq(pageable));
        verify(userMapper, times(0)).toDto(any(User.class));
    }

}
