package pl.com.chrzanowski.sma.unitTests.role.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.role.dao.RoleDao;
import pl.com.chrzanowski.sma.role.dao.RoleJPADaoImpl;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;
import pl.com.chrzanowski.sma.role.service.filter.RoleQuerySpec;

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

    @Mock
    private RoleQuerySpec roleQuerySpec;

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
        Role role1 = Role.builder().name(ERole.ROLE_USER.getRoleName()).build();
        Role role2 = Role.builder().name(ERole.ROLE_ADMIN.getRoleName()).build();
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
        String roleName = ERole.ROLE_USER.getRoleName();
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
        String roleName = ERole.ROLE_USER.getRoleName();
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
        Role role = Role.builder().name(ERole.ROLE_USER.getRoleName()).build();
        Role savedRole = Role.builder().id(1L).name(ERole.ROLE_USER.getRoleName()).build();
        when(roleRepository.save(role)).thenReturn(savedRole);

        // When
        Role returnedRole = roleJPADaoImpl.saveRole(role);

        // Then
        assertEquals(returnedRole, savedRole);
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void saveRole_Negative() {
        // Given
        Role role = Role.builder().name(ERole.ROLE_USER.getRoleName()).build();
        when(roleRepository.save(role)).thenThrow(new RuntimeException("Save failed"));

        // When / Then
        assertThrows(RuntimeException.class, () -> roleJPADaoImpl.saveRole(role));
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testDeleteById_Success() {
        Long roleId = 3L;

        assertDoesNotThrow(() -> roleJPADaoImpl.deleteById(roleId));

        verify(roleRepository, times(1)).deleteById(roleId);
    }

    @Test
    void findAll_WithSpecification_Positive() {
        // Given
        BooleanBuilder specification = mock(BooleanBuilder.class);
        JPQLQuery<Role> query = mock(JPQLQuery.class);
        Role role = Role.builder().name("ROLE_USER").build();
        when(roleQuerySpec.buildQuery(specification)).thenReturn(query);
        when(query.fetch()).thenReturn(List.of(role));

        // When
        List<Role> roles = roleJPADaoImpl.findAll(specification);

        // Then
        assertEquals(1, roles.size());
        assertTrue(roles.contains(role));
        verify(roleQuerySpec, times(1)).buildQuery(specification);
        verify(query, times(1)).fetch();
    }

    @Test
    void findAll_WithSpecification_Negative() {
        // Given
        BooleanBuilder specification = mock(BooleanBuilder.class);
        JPQLQuery<Role> query = mock(JPQLQuery.class);
        when(roleQuerySpec.buildQuery(specification)).thenReturn(query);
        when(query.fetch()).thenReturn(Collections.emptyList());

        // When
        List<Role> roles = roleJPADaoImpl.findAll(specification);

        // Then
        assertTrue(roles.isEmpty());
        verify(roleQuerySpec, times(1)).buildQuery(specification);
        verify(query, times(1)).fetch();
    }

    @Test
    void findAll_WithSpecificationAndPageable_Positive() {
        // Given
        BooleanBuilder specification = mock(BooleanBuilder.class);
        JPQLQuery<Role> query = mock(JPQLQuery.class);
        Pageable pageable = PageRequest.of(0, 2);
        Role role = Role.builder().name("ROLE_ADMIN").build();

        when(roleQuerySpec.buildQuery(specification)).thenReturn(query);
        when(query.offset(pageable.getOffset())).thenReturn(query);
        when(query.limit(pageable.getPageSize())).thenReturn(query);
        when(query.fetchCount()).thenReturn(1L);
        when(query.fetch()).thenReturn(List.of(role));

        // When
        Page<Role> rolesPage = roleJPADaoImpl.findAll(specification, pageable);

        // Then
        assertEquals(1, rolesPage.getTotalElements());
        assertEquals(1, rolesPage.getContent().size());
        assertTrue(rolesPage.getContent().contains(role));
        verify(roleQuerySpec, times(1)).buildQuery(specification);
        verify(query, times(1)).offset(pageable.getOffset());
        verify(query, times(1)).limit(pageable.getPageSize());
        verify(query, times(1)).fetchCount();
        verify(query, times(1)).fetch();
    }

    @Test
    void findAll_WithSpecificationAndPageable_Negative() {
        // Given
        BooleanBuilder specification = mock(BooleanBuilder.class);
        JPQLQuery<Role> query = mock(JPQLQuery.class);
        Pageable pageable = PageRequest.of(0, 2);

        when(roleQuerySpec.buildQuery(specification)).thenReturn(query);
        when(query.offset(pageable.getOffset())).thenReturn(query);
        when(query.limit(pageable.getPageSize())).thenReturn(query);
        when(query.fetchCount()).thenReturn(0L);
        when(query.fetch()).thenReturn(Collections.emptyList());

        // When
        Page<Role> rolesPage = roleJPADaoImpl.findAll(specification, pageable);

        // Then
        assertTrue(rolesPage.isEmpty());
        verify(roleQuerySpec, times(1)).buildQuery(specification);
        verify(query, times(1)).offset(pageable.getOffset());
        verify(query, times(1)).limit(pageable.getPageSize());
        verify(query, times(1)).fetchCount();
        verify(query, times(1)).fetch();
    }

    @Test
    void findById_Positive() {
        // Given
        Long id = 1L;
        Role role = Role.builder().id(id).name("ROLE_MANAGER").build();
        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        // When
        Optional<Role> foundRole = roleJPADaoImpl.findById(id);

        // Then
        assertTrue(foundRole.isPresent());
        assertEquals(role, foundRole.get());
        verify(roleRepository, times(1)).findById(id);
    }

    @Test
    void findById_Negative() {
        // Given
        Long id = 99L;
        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Role> foundRole = roleJPADaoImpl.findById(id);

        // Then
        assertTrue(foundRole.isEmpty());
        verify(roleRepository, times(1)).findById(id);
    }
}