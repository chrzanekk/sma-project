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

        roleUser = Role.builder().id(1L).name(ERole.ROLE_USER.getRoleName()).build();

        roleUserDTO = RoleDTO.builder().id(1L).name(ERole.ROLE_USER.getRoleName()).build();
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
        assertTrue(roles.stream().anyMatch(role -> Objects.equals(role.getName(), ERole.ROLE_USER.getRoleName())));

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
        when(roleDao.findByName(ERole.ROLE_USER.getRoleName())).thenReturn(Optional.of(roleUser));
        when(roleMapper.toDto(roleUser)).thenReturn(roleUserDTO);

        RoleDTO result = roleService.findByName(ERole.ROLE_USER.getRoleName());

        assertNotNull(result);
        assertEquals(ERole.ROLE_USER.getRoleName(), result.getName());

        verify(roleDao, times(1)).findByName(ERole.ROLE_USER.getRoleName());
        verify(roleMapper, times(1)).toDto(roleUser);
    }

    @Test
    void testFindByNameRoleNotFound() {
        when(roleDao.findByName(ERole.ROLE_USER.getRoleName())).thenReturn(Optional.empty());

        assertThrows(RoleException.class, () -> roleService.findByName(ERole.ROLE_USER.getRoleName()));

        verify(roleDao, times(1)).findByName(ERole.ROLE_USER.getRoleName());
    }

    @Test
    void testSaveRoleSuccess() {
        ArgumentCaptor<RoleDTO> captor = ArgumentCaptor.forClass(RoleDTO.class);

        when(roleMapper.toEntity(any(RoleDTO.class))).thenReturn(roleUser);
        when(roleDao.saveRole(any(Role.class))).thenReturn(roleUser);
        when(roleMapper.toDto(any(Role.class))).thenReturn(roleUserDTO);

        RoleDTO result = roleService.saveRole(roleUserDTO);

        assertNotNull(result);
        assertEquals(ERole.ROLE_USER.getRoleName(), result.getName());

        verify(roleMapper).toEntity(captor.capture());
        RoleDTO capturedDTO = captor.getValue();
        assertEquals(ERole.ROLE_USER.getRoleName(), capturedDTO.getName());
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
        Role role = Role.builder().id(roleId).name(ERole.ROLE_ADMIN.getRoleName()).build();

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

}
