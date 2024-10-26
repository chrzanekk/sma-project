package pl.com.chrzanowski.sma.role.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleJPADaoImplTest {

    @InjectMocks
    private RoleJPADaoImpl roleJPADaoImpl;

    @Mock
    private RoleRepository roleRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findAll_Positive() {
        // Given
        Role role1 = Role.builder().name(ERole.ROLE_USER).build();
        Role role2 = Role.builder().name(ERole.ROLE_ADMIN).build();
        when(roleRepository.findAll()).thenReturn(List.of(role1, role2));

        // When
        List<Role> roles = roleJPADaoImpl.findAll();

        // Then
        assertEquals(2, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void findAll_Negative() {
        // Given
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Role> roles = roleJPADaoImpl.findAll();

        // Then
        assertTrue(roles.isEmpty());
        verify(roleRepository, times(1)).findAll();
    }

    // Testy dla findByName()
    @Test
    void findByName_Positive() {
        // Given
        ERole roleName = ERole.ROLE_USER;
        Role role = Role.builder().name(roleName).build();
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));

        // When
        Optional<Role> foundRole = roleJPADaoImpl.findByName(roleName);

        // Then
        assertTrue(foundRole.isPresent());
        assertEquals(role, foundRole.get());
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    void findByName_Negative() {
        // Given
        ERole roleName = ERole.ROLE_MODERATOR;
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        // When
        Optional<Role> foundRole = roleJPADaoImpl.findByName(roleName);

        // Then
        assertTrue(foundRole.isEmpty());
        verify(roleRepository, times(1)).findByName(roleName);
    }

    // Testy dla saveRole()
    @Test
    void saveRole_Positive() {
        // Given
        Role role = Role.builder().name(ERole.ROLE_USER).build();
        when(roleRepository.save(role)).thenReturn(role);

        // When
        Role savedRole = roleJPADaoImpl.saveRole(role);

        // Then
        assertEquals(role, savedRole);
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void saveRole_Negative() {
        // Given
        Role role = Role.builder().name(ERole.ROLE_USER).build();
        when(roleRepository.save(role)).thenThrow(new RuntimeException("Save failed"));

        // When / Then
        assertThrows(RuntimeException.class, () -> roleJPADaoImpl.saveRole(role));
        verify(roleRepository, times(1)).save(role);
    }
}