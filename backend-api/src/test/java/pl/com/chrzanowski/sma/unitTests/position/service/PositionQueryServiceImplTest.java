package pl.com.chrzanowski.sma.unitTests.position.service;

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
import pl.com.chrzanowski.sma.position.dao.PositionDao;
import pl.com.chrzanowski.sma.position.dto.PositionAuditableDTO;
import pl.com.chrzanowski.sma.position.dto.PositionBaseDTO;
import pl.com.chrzanowski.sma.position.mapper.PositionAuditMapper;
import pl.com.chrzanowski.sma.position.mapper.PositionBaseMapper;
import pl.com.chrzanowski.sma.position.model.Position;
import pl.com.chrzanowski.sma.position.service.PositionQueryServiceImpl;
import pl.com.chrzanowski.sma.position.service.filter.PositionFilter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PositionQueryServiceImplTest {

    @Mock
    private PositionDao positionDao;

    @Mock
    private PositionBaseMapper positionBaseMapper;

    @Mock
    private PositionAuditMapper positionAuditMapper;

    @InjectMocks
    private PositionQueryServiceImpl positionQueryService;

    private PositionBaseDTO positionBaseDTO;
    private PositionAuditableDTO positionAuditableDTO;
    private Position position;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        positionBaseDTO = PositionBaseDTO.builder()
                .id(1L)
                .name("Manager")
                .build();

        positionAuditableDTO = PositionAuditableDTO.builder()
                .base(positionBaseDTO)
                .build();

        position = new Position();
        position.setId(1L);
        position.setName("Manager");
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testFindByFilterSuccess() {
        PositionFilter filter = new PositionFilter();

        when(positionDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(position));
        when(positionAuditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(positionAuditableDTO));

        List<PositionAuditableDTO> result = positionQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Manager", result.get(0).getBase().getName());

        verify(positionDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(positionAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterEmpty() {
        PositionFilter filter = new PositionFilter();

        when(positionDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(positionAuditMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<PositionAuditableDTO> result = positionQueryService.findByFilter(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(positionDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(positionAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        PositionFilter filter = new PositionFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Position> positionPage = new PageImpl<>(Collections.singletonList(position));
        when(positionDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(positionPage);
        when(positionAuditMapper.toDto(any(Position.class))).thenReturn(positionAuditableDTO);

        Page<PositionAuditableDTO> result = positionQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Manager", result.getContent().get(0).getBase().getName());

        verify(positionDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(positionAuditMapper, times(1)).toDto(any(Position.class));
    }

    @Test
    void testFindByFilterAndPageEmpty() {
        PositionFilter filter = new PositionFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Position> positionPage = Page.empty();
        when(positionDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(positionPage);

        Page<PositionAuditableDTO> result = positionQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(positionDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(positionAuditMapper, never()).toDto(any(Position.class));
    }

    @Test
    void testFindByFilterWithCriteria() {
        PositionFilter filter = PositionFilter.builder()
                .nameContains("Manager")
                .build();

        when(positionDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(position));
        when(positionAuditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(positionAuditableDTO));

        List<PositionAuditableDTO> result = positionQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(positionDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(positionAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageWithCriteria() {
        PositionFilter filter = PositionFilter.builder()
                .nameContains("Manager")
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Position> positionPage = new PageImpl<>(Collections.singletonList(position), pageable, 1);
        when(positionDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(positionPage);
        when(positionAuditMapper.toDto(any(Position.class))).thenReturn(positionAuditableDTO);

        Page<PositionAuditableDTO> result = positionQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        verify(positionDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(positionAuditMapper, times(1)).toDto(any(Position.class));
    }
}
