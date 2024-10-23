package pl.com.chrzanowski.sma.role;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.enumeration.ERole;
import pl.com.chrzanowski.sma.exception.RoleException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

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

        roleUser = Role.builder().id(1L).name(ERole.ROLE_USER).build();

        roleUserDTO = RoleDTO.builder().id(1L).name(ERole.ROLE_USER).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    // Testy dla metody findAll()

    @Test
    void testFindAllSuccess() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(roleUser));
        when(roleMapper.toDto(anySet())).thenReturn(Set.of(roleUserDTO));


        Set<RoleDTO> roles = roleService.findAll();

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertTrue(roles.stream().anyMatch(role -> role.getName() == ERole.ROLE_USER));

        verify(roleRepository, times(1)).findAll();
        verify(roleMapper, times(1)).toDto(anySet());
    }

    @Test
    void testFindAllEmptyList() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());
        Set<RoleDTO> roles = roleService.findAll();

        assertTrue(roles.isEmpty());

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testFindByNameSuccess() {
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(roleUser));
        when(roleMapper.toDto(roleUser)).thenReturn(roleUserDTO);

        RoleDTO result = roleService.findByName(ERole.ROLE_USER);

        assertNotNull(result);
        assertEquals(ERole.ROLE_USER, result.getName());

        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
        verify(roleMapper, times(1)).toDto(roleUser);
    }

    @Test
    void testFindByNameRoleNotFound() {
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.empty());
        assertThrows(RoleException.class, () -> roleService.findByName(ERole.ROLE_USER));

        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
    }

    @Test
    void testSaveRoleSuccess() {
        when(roleMapper.toEntity(roleUserDTO)).thenReturn(roleUser);
        when(roleRepository.save(roleUser)).thenReturn(roleUser);

        when(roleMapper.toDto(roleUser)).thenReturn(roleUserDTO);

        RoleDTO result = roleService.saveRole(roleUserDTO);

        assertNotNull(result);
        assertEquals(ERole.ROLE_USER, result.getName());

        verify(roleMapper, times(1)).toEntity(roleUserDTO);
        verify(roleRepository, times(1)).save(roleUser);
        verify(roleMapper, times(1)).toDto(roleUser);
    }

    @Test
    void testSaveRoleInvalidRole() {

        RoleDTO invalidRoleDTO = RoleDTO.builder().build();

        assertThrows(RoleException.class, () -> roleService.saveRole(invalidRoleDTO));

        verify(roleRepository, never()).save(any(Role.class));
    }
}
