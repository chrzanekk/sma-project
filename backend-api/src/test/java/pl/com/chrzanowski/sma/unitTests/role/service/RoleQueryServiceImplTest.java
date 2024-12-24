package pl.com.chrzanowski.sma.unitTests.role.service;

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
import pl.com.chrzanowski.sma.role.dao.RoleDao;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.service.RoleQueryServiceImpl;
import pl.com.chrzanowski.sma.role.service.filter.RoleFilter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleQueryServiceImplTest {

    @Mock
    private RoleDao roleDao;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleQueryServiceImpl roleQueryService;

    private AutoCloseable autoCloseable;
    private Role role;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        role = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        roleDTO = RoleDTO.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testFindByFilter_Positive() {
        // Given
        RoleFilter filter = RoleFilter.builder()
                .name("USER")
                .build();
        when(roleDao.findAll(any(BooleanBuilder.class))).thenReturn(List.of(role));
        when(roleMapper.toDtoList(anyList())).thenReturn(List.of(roleDTO));

        // When
        List<RoleDTO> result = roleQueryService.findByFilter(filter);

        // Then
        assertEquals(1, result.size());
        assertEquals("ROLE_USER", result.get(0).getName());
        verify(roleDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(roleMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilter_Negative() {
        // Given
        RoleFilter filter = RoleFilter.builder()
                .name("NON_EXISTENT")
                .build();
        when(roleDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(roleMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        // When
        List<RoleDTO> result = roleQueryService.findByFilter(filter);

        // Then
        assertTrue(result.isEmpty());
        verify(roleDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(roleMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterWithPagination_Positive() {
        // Given
        RoleFilter filter = RoleFilter.builder()
                .name("USER")
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        when(roleDao.findAll(any(BooleanBuilder.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(role)));
        when(roleMapper.toDto(role)).thenReturn(roleDTO);

        // When
        Page<RoleDTO> result = roleQueryService.findByFilter(filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("ROLE_USER", result.getContent().get(0).getName());
        verify(roleDao, times(1)).findAll(any(BooleanBuilder.class), eq(pageable));
        verify(roleMapper, times(1)).toDto(any(Role.class));
    }

    @Test
    void testFindByFilterWithPagination_Negative() {
        // Given
        RoleFilter filter = RoleFilter.builder()
                .name("NON_EXISTENT")
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        when(roleDao.findAll(any(BooleanBuilder.class), eq(pageable)))
                .thenReturn(Page.empty(pageable));

        // When
        Page<RoleDTO> result = roleQueryService.findByFilter(filter, pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roleDao, times(1)).findAll(any(BooleanBuilder.class), eq(pageable));
        verify(roleMapper, times(0)).toDto(any(Role.class));
    }

    @Test
    void testFindByFilterWithMultipleRoles() {
        // Given
        Role roleAdmin = Role.builder()
                .id(2L)
                .name("ROLE_ADMIN")
                .build();
        RoleDTO roleAdminDTO = RoleDTO.builder()
                .id(2L)
                .name("ROLE_ADMIN")
                .build();

        RoleFilter filter = RoleFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        when(roleDao.findAll(any(BooleanBuilder.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(role, roleAdmin)));
        when(roleMapper.toDto(role)).thenReturn(roleDTO);
        when(roleMapper.toDto(roleAdmin)).thenReturn(roleAdminDTO);

        // When
        Page<RoleDTO> result = roleQueryService.findByFilter(filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("ROLE_USER", result.getContent().get(0).getName());
        assertEquals("ROLE_ADMIN", result.getContent().get(1).getName());

        verify(roleDao, times(1)).findAll(any(BooleanBuilder.class), eq(pageable));
        verify(roleMapper, times(2)).toDto(any(Role.class));
    }
}
