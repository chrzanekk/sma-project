package pl.com.chrzanowski.sma.unitTests.unit.service;

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
import pl.com.chrzanowski.sma.unit.dao.UnitDao;
import pl.com.chrzanowski.sma.unit.dto.UnitAuditableDTO;
import pl.com.chrzanowski.sma.unit.dto.UnitBaseDTO;
import pl.com.chrzanowski.sma.unit.dto.UnitDTO;
import pl.com.chrzanowski.sma.unit.mapper.UnitAuditMapper;
import pl.com.chrzanowski.sma.unit.model.Unit;
import pl.com.chrzanowski.sma.unit.service.UnitQueryServiceImpl;
import pl.com.chrzanowski.sma.unit.service.filter.UnitFilter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UnitQueryServiceImplTest {

    @Mock
    private UnitDao unitDao;

    @Mock
    private UnitAuditMapper unitAuditMapper;

    @InjectMocks
    private UnitQueryServiceImpl unitQueryService;

    private UnitBaseDTO unitBaseDTO;
    private UnitAuditableDTO unitAuditableDTO;
    private UnitDTO unitDTO;
    private Unit unit;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        unitBaseDTO = UnitBaseDTO.builder()
                .id(1L)
                .symbol("KM2")
                .build();

        unitDTO = UnitDTO.builder()
                .id(1L)
                .symbol("KM2")
                .build();

        unitAuditableDTO = UnitAuditableDTO.builder()
                .base(unitDTO)
                .build();

        unit = new Unit();
        unit.setId(1L);
        unit.setSymbol("KM2");
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testFindByFilterSuccess() {
        UnitFilter filter = new UnitFilter();

        when(unitDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(unit));
        when(unitAuditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(unitAuditableDTO));

        List<UnitAuditableDTO> result = unitQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("KM2", result.get(0).getBase().getSymbol());

        verify(unitDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(unitAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterEmpty() {
        UnitFilter filter = new UnitFilter();

        when(unitDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(unitAuditMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<UnitAuditableDTO> result = unitQueryService.findByFilter(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(unitDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(unitAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        UnitFilter filter = new UnitFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Unit> positionPage = new PageImpl<>(Collections.singletonList(unit));
        when(unitDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(positionPage);
        when(unitAuditMapper.toDto(any(Unit.class))).thenReturn(unitAuditableDTO);

        Page<UnitAuditableDTO> result = unitQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("KM2", result.getContent().get(0).getBase().getSymbol());

        verify(unitDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(unitAuditMapper, times(1)).toDto(any(Unit.class));
    }

    @Test
    void testFindByFilterAndPageEmpty() {
        UnitFilter filter = new UnitFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Unit> positionPage = Page.empty();
        when(unitDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(positionPage);

        Page<UnitAuditableDTO> result = unitQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(unitDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(unitAuditMapper, never()).toDto(any(Unit.class));
    }

    @Test
    void testFindByFilterWithCriteria() {
        UnitFilter filter = UnitFilter.builder()
                .symbolContains("KM2")
                .build();

        when(unitDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(unit));
        when(unitAuditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(unitAuditableDTO));

        List<UnitAuditableDTO> result = unitQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(unitDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(unitAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageWithCriteria() {
        UnitFilter filter = UnitFilter.builder()
                .symbolContains("KM2")
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Unit> positionPage = new PageImpl<>(Collections.singletonList(unit), pageable, 1);
        when(unitDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(positionPage);
        when(unitAuditMapper.toDto(any(Unit.class))).thenReturn(unitAuditableDTO);

        Page<UnitAuditableDTO> result = unitQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        verify(unitDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(unitAuditMapper, times(1)).toDto(any(Unit.class));
    }
}
