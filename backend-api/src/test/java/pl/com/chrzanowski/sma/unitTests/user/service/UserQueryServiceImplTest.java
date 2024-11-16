package pl.com.chrzanowski.sma.unitTests.user.service;

import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.user.dao.UserDao;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserQueryServiceImpl;
import pl.com.chrzanowski.sma.user.service.filter.UserFilter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserQueryServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private UserMapper userMapper;


    @InjectMocks
    private UserQueryServiceImpl userQueryService;

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
                .firstName("firstName")
                .lastName("lastName")
                .position("position")
                .build();
        registerRequest = new RegisterRequest();
        registerRequest.setLogin("testUser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setFirstName("firstName");
        registerRequest.setLastName("lastName");
        registerRequest.setPosition("position");
        registerRequest.setPassword("password");

        roleUser = Role.builder().id(1L).name(ERole.ROLE_USER.getRoleName()).build();
        user = User.builder().id(1L)
                .login("testUser")
                .email("test@example.com")
                .firstName("firstName")
                .lastName("lastName")
                .position("position")
                .roles(Set.of(roleUser)).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }


    @Test
    void testFindByFilter() {
        UserFilter filter = UserFilter.builder()
                .emailStartsWith("test")
                .isEnabled(true).build();
        when(userDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(user));
        when(userMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(userDTO));


        List<UserDTO> result = userQueryService.findByFilter(filter);

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getLogin());

        verify(userDao, times(1)).findAll(any(BooleanBuilder.class));

        verify(userMapper, times(1)).toDtoList(anyList());
    }


    @Test
    void testFindByFilter_SuccessWithEmail() {
        UserFilter filter = UserFilter.builder()
                .emailStartsWith("test")
                .isEnabled(true).build();
        Pageable pageable = PageRequest.of(0, 10);

        when(userDao.findAll(any(BooleanBuilder.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        Page<UserDTO> result = userQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testUser", result.getContent().get(0).getLogin());

        verify(userDao, times(1)).findAll(any(BooleanBuilder.class), eq(pageable));
        verify(userMapper, times(1)).toDto(any(User.class));
    }

    @Test
    void testFindByFilter_SuccessWithLogin() {
        UserFilter filter = UserFilter.builder()
                .loginStartsWith("test")
                .isEnabled(true).build();
        Pageable pageable = PageRequest.of(0, 10);

        when(userDao.findAll(any(BooleanBuilder.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        Page<UserDTO> result = userQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testUser", result.getContent().get(0).getLogin());

        verify(userDao, times(1)).findAll(any(BooleanBuilder.class), eq(pageable));
        verify(userMapper, times(1)).toDto(any(User.class));
    }

    @Test
    void testFindByFilter_SuccessWithFirstName() {
        UserFilter filter = UserFilter.builder()
                .firstNameStartsWith("first")
                .isEnabled(true).build();
        Pageable pageable = PageRequest.of(0, 10);

        when(userDao.findAll(any(BooleanBuilder.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        Page<UserDTO> result = userQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testUser", result.getContent().get(0).getLogin());

        verify(userDao, times(1)).findAll(any(BooleanBuilder.class), eq(pageable));
        verify(userMapper, times(1)).toDto(any(User.class));
    }

    @Test
    void testFindByFilter_SuccessWithLastName() {
        UserFilter filter = UserFilter.builder()
                .lastNameStartsWith("last")
                .isEnabled(true).build();
        Pageable pageable = PageRequest.of(0, 10);

        when(userDao.findAll(any(BooleanBuilder.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        Page<UserDTO> result = userQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testUser", result.getContent().get(0).getLogin());

        verify(userDao, times(1)).findAll(any(BooleanBuilder.class), eq(pageable));
        verify(userMapper, times(1)).toDto(any(User.class));
    }

    @Test
    void testFindByFilter_SuccessWithPosition() {
        UserFilter filter = UserFilter.builder()
                .positionStartsWith("pos")
                .isEnabled(true).build();
        Pageable pageable = PageRequest.of(0, 10);

        when(userDao.findAll(any(BooleanBuilder.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        Page<UserDTO> result = userQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testUser", result.getContent().get(0).getLogin());

        verify(userDao, times(1)).findAll(any(BooleanBuilder.class), eq(pageable));
        verify(userMapper, times(1)).toDto(any(User.class));
    }

    @Test
    void testFindByFilter_NoResults() {
        UserFilter filter = UserFilter.builder()
                .emailStartsWith("test")
                .isEnabled(true).build();
        Pageable pageable = PageRequest.of(0, 10);
        when(userDao.findAll(any(BooleanBuilder.class), eq(pageable)))
                .thenReturn(Page.empty(pageable));


        Page<UserDTO> result = userQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Result should be empty");

        verify(userDao, times(1)).findAll(any(BooleanBuilder.class), eq(pageable));
        verify(userMapper, times(0)).toDto(any(User.class));
    }
}
