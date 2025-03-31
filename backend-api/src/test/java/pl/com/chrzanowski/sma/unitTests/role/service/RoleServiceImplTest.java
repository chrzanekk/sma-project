package pl.com.chrzanowski.sma.unitTests.role.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.common.exception.RoleException;
import pl.com.chrzanowski.sma.role.dao.RoleDao;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.service.RoleServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleDao roleDao;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role roleUser;
    private RoleDTO roleUserDTO;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        roleUser = Role.builder().id(1L).name(ERole.ROLE_USER.getName()).build();

        roleUserDTO = RoleDTO.builder().id(1L).name(ERole.ROLE_USER.getName()).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testFindAllSuccess() {
        when(roleDao.findAll()).thenReturn(Arrays.asList(roleUser));
        when(roleMapper.toDtoList(anyList())).thenReturn(List.of(roleUserDTO));

        List<RoleDTO> roles = roleService.findAll();

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertTrue(roles.stream().anyMatch(role -> Objects.equals(role.getName(), ERole.ROLE_USER.getName())));

        verify(roleDao, times(1)).findAll();
        verify(roleMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindAllEmptyList() {
        when(roleDao.findAll()).thenReturn(Collections.emptyList());

        List<RoleDTO> roles = roleService.findAll();

        assertTrue(roles.isEmpty());

        verify(roleDao, times(1)).findAll();
    }

    @Test
    void testFindByNameSuccess() {
        when(roleDao.findByName(ERole.ROLE_USER.getName())).thenReturn(Optional.of(roleUser));
        when(roleMapper.toDto(roleUser)).thenReturn(roleUserDTO);

        RoleDTO result = roleService.findByName(ERole.ROLE_USER.getName());

        assertNotNull(result);
        assertEquals(ERole.ROLE_USER.getName(), result.getName());

        verify(roleDao, times(1)).findByName(ERole.ROLE_USER.getName());
        verify(roleMapper, times(1)).toDto(roleUser);
    }

    @Test
    void testFindByNameRoleNotFound() {
        when(roleDao.findByName(ERole.ROLE_USER.getName())).thenReturn(Optional.empty());

        assertThrows(RoleException.class, () -> roleService.findByName(ERole.ROLE_USER.getName()));

        verify(roleDao, times(1)).findByName(ERole.ROLE_USER.getName());
    }

    @Test
    void testSaveRoleSuccess() {
        ArgumentCaptor<RoleDTO> captor = ArgumentCaptor.forClass(RoleDTO.class);

        when(roleMapper.toEntity(any(RoleDTO.class))).thenReturn(roleUser);
        when(roleDao.saveRole(any(Role.class))).thenReturn(roleUser);
        when(roleMapper.toDto(any(Role.class))).thenReturn(roleUserDTO);

        RoleDTO result = roleService.saveRole(roleUserDTO);

        assertNotNull(result);
        assertEquals(ERole.ROLE_USER.getName(), result.getName());

        verify(roleMapper).toEntity(captor.capture());
        RoleDTO capturedDTO = captor.getValue();
        assertEquals(ERole.ROLE_USER.getName(), capturedDTO.getName());
        assertNotNull(capturedDTO.getCreatedDatetime());
    }

    @Test
    void testSaveRoleWhenRoleNameIsNull() {
        RoleDTO roleWithoutName = RoleDTO.builder().build();

        RoleException exception = assertThrows(RoleException.class, () -> {
            roleService.saveRole(roleWithoutName);
        });
        assertEquals("Error RoleName not found", exception.getMessage());

        verify(roleDao,never()).saveRole(any(Role.class));
    }

    @Test
    void testDeleteById_Success() {
        Long roleId = 3L;
        Role role = Role.builder().id(roleId).name("ROLE_CUSTOMER").build();

        when(roleDao.findById(roleId)).thenReturn(Optional.of(role));

        assertDoesNotThrow(() -> roleService.deleteById(roleId));

        verify(roleDao, times(1)).findById(roleId);
        verify(roleDao, times(1)).deleteById(roleId);
    }

    @Test
    void testDeleteById_CannotDeleteAdminRole() {
        Long roleId = 2L;
        Role role = Role.builder().id(roleId).name(ERole.ROLE_ADMIN.getName()).build();

        when(roleDao.findById(roleId)).thenReturn(Optional.of(role));

        RoleException exception = assertThrows(RoleException.class, () -> roleService.deleteById(roleId));
        assertEquals("Cannot delete role ROLE_ADMIN", exception.getMessage());

        verify(roleDao, times(1)).findById(roleId);
        verify(roleDao, times(0)).deleteById(roleId);
    }

    @Test
    void testDeleteById_RoleNotFound() {
        Long roleId = 5L;

        when(roleDao.findById(roleId)).thenReturn(Optional.empty());

        RoleException exception = assertThrows(RoleException.class, () -> roleService.deleteById(roleId));
        assertEquals("Error Role not found", exception.getMessage());

        verify(roleDao, times(1)).findById(roleId);
        verify(roleDao, times(0)).deleteById(roleId);
    }

    @Test
    void testFindAllByListOfNames_Success() {
        // Arrange
        List<String> roleNames = List.of(ERole.ROLE_USER.getName(), ERole.ROLE_ADMIN.getName());

        Role roleUser = Role.builder().id(1L).name(ERole.ROLE_USER.getName()).build();
        Role roleAdmin = Role.builder().id(2L).name(ERole.ROLE_ADMIN.getName()).build();

        RoleDTO roleUserDTO = RoleDTO.builder().id(1L).name(ERole.ROLE_USER.getName()).build();
        RoleDTO roleAdminDTO = RoleDTO.builder().id(2L).name(ERole.ROLE_ADMIN.getName()).build();

        when(roleDao.findByName(ERole.ROLE_USER.getName())).thenReturn(Optional.of(roleUser));
        when(roleDao.findByName(ERole.ROLE_ADMIN.getName())).thenReturn(Optional.of(roleAdmin));

        when(roleMapper.toDto(roleUser)).thenReturn(roleUserDTO);
        when(roleMapper.toDto(roleAdmin)).thenReturn(roleAdminDTO);

        // Act
        Set<RoleDTO> result = roleService.findAllByListOfNames(roleNames);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(role -> role.getName().equals(ERole.ROLE_USER.getName())));
        assertTrue(result.stream().anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN.getName())));

        verify(roleDao, times(1)).findByName(ERole.ROLE_USER.getName());
        verify(roleDao, times(1)).findByName(ERole.ROLE_ADMIN.getName());
    }

    @Test
    void testFindAllByListOfNames_RoleNotFound() {
        // Arrange
        List<String> roleNames = List.of("INVALID_ROLE");

        when(roleDao.findByName("INVALID_ROLE")).thenReturn(Optional.empty());

        // Act & Assert
        RoleException exception = assertThrows(RoleException.class, () -> roleService.findAllByListOfNames(roleNames));
        assertEquals("Error: Role not found: INVALID_ROLE", exception.getMessage());

        verify(roleDao, times(1)).findByName("INVALID_ROLE");
    }

    @Test
    void testFindAllByListOfNames_EmptyInput() {
        // Arrange
        List<String> roleNames = Collections.emptyList();

        // Act
        Set<RoleDTO> result = roleService.findAllByListOfNames(roleNames);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(roleDao, never()).findByName(anyString());
    }


}
