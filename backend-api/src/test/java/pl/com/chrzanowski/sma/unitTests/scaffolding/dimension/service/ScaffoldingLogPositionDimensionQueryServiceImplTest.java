package pl.com.chrzanowski.sma.unitTests.scaffolding.dimension.service;

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
import pl.com.chrzanowski.sma.common.enumeration.DimensionType;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingOperationType;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingType;
import pl.com.chrzanowski.sma.common.enumeration.TechnicalProtocolStatus;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.dao.ScaffoldingLogPositionDimensionDao;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.mapper.ScaffoldingLogPositionDimensionAuditMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;
import pl.com.chrzanowski.sma.scaffolding.dimension.service.ScaffoldingLogPositionDimensionQueryServiceImpl;
import pl.com.chrzanowski.sma.scaffolding.dimension.service.filter.ScaffoldingLogPositionDimensionFilter;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ScaffoldingLogPositionDimensionQueryServiceImplTest {

    @Mock
    private ScaffoldingLogPositionDimensionDao dao;

    @Mock
    private ScaffoldingLogPositionDimensionAuditMapper auditMapper;

    @InjectMocks
    private ScaffoldingLogPositionDimensionQueryServiceImpl dimensionQueryService;

    private ScaffoldingLogPositionDimensionDTO dimensionDTO;
    private ScaffoldingLogPositionDimensionAuditableDTO dimensionAuditableDTO;
    private ScaffoldingLogPositionDimension dimension;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        // Tworzenie ScaffoldingLogBaseDTO
        ScaffoldingLogBaseDTO scaffoldingLogBaseDTO = ScaffoldingLogBaseDTO.builder()
                .id(1L)
                .name("Test Log")
                .additionalInfo("Additional info")
                .build();

        // Tworzenie ScaffoldingLogPositionBaseDTO
        ScaffoldingLogPositionBaseDTO scaffoldingPosition = ScaffoldingLogPositionBaseDTO.builder()
                .id(1L)
                .scaffoldingNumber("SC-001")
                .assemblyLocation("Warsaw")
                .assemblyDate(LocalDate.of(2025, 1, 15))
                .dismantlingDate(LocalDate.of(2025, 6, 30))
                .dismantlingNotificationDate(LocalDate.of(2025, 6, 20))
                .scaffoldingType(ScaffoldingType.BASIC)
                .scaffoldingFullDimension(new BigDecimal("100.50"))
                .technicalProtocolStatus(TechnicalProtocolStatus.CREATED)
                .parentPosition(null)
                .childPositions(Collections.emptyList())
                .scaffoldingLog(scaffoldingLogBaseDTO)
                .build();

        // Tworzenie CompanyBaseDTO
        CompanyBaseDTO company = CompanyBaseDTO.builder()
                .id(1L)
                .name("Test Company")
                .additionalInfo("Company info")
                .build();

        // Tworzenie ScaffoldingLogPositionDimensionDTO
        dimensionDTO = ScaffoldingLogPositionDimensionDTO.builder()
                .id(1L)
                .height(new BigDecimal("10.5"))
                .width(new BigDecimal("5.0"))
                .length(new BigDecimal("15.0"))
                .dimensionType(DimensionType.BASIC_STRUCTURE)
                .dismantlingDate(LocalDate.of(2025, 6, 30))
                .scaffoldingPosition(scaffoldingPosition)
                .operationType(ScaffoldingOperationType.ASSEMBLY)
                .company(company)
                .build();

        dimensionAuditableDTO = ScaffoldingLogPositionDimensionAuditableDTO.builder()
                .base(dimensionDTO)
                .build();

        // Tworzenie encji ScaffoldingLogPositionDimension
        dimension = new ScaffoldingLogPositionDimension();
        dimension.setId(1L);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testFindByFilterSuccess() {
        ScaffoldingLogPositionDimensionFilter filter = new ScaffoldingLogPositionDimensionFilter();

        when(dao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(dimension));
        when(auditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(dimensionAuditableDTO));

        List<ScaffoldingLogPositionDimensionAuditableDTO> result = dimensionQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0, new BigDecimal("10.5").compareTo(result.getFirst().getBase().getHeight()));
        assertEquals(DimensionType.BASIC_STRUCTURE, result.getFirst().getBase().getDimensionType());
        assertEquals("Test Company", result.getFirst().getBase().getCompany().getName());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class));
        verify(auditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterEmpty() {
        ScaffoldingLogPositionDimensionFilter filter = new ScaffoldingLogPositionDimensionFilter();

        when(dao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(auditMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<ScaffoldingLogPositionDimensionAuditableDTO> result = dimensionQueryService.findByFilter(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class));
        verify(auditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        ScaffoldingLogPositionDimensionFilter filter = new ScaffoldingLogPositionDimensionFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<ScaffoldingLogPositionDimension> dimensionPage = new PageImpl<>(Collections.singletonList(dimension));
        when(dao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(dimensionPage);
        when(auditMapper.toDto(any(ScaffoldingLogPositionDimension.class))).thenReturn(dimensionAuditableDTO);

        Page<ScaffoldingLogPositionDimensionAuditableDTO> result = dimensionQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(0, new BigDecimal("15.0").compareTo(result.getContent().getFirst().getBase().getLength()));
        assertEquals("SC-001", result.getContent().getFirst().getBase().getScaffoldingPosition().getScaffoldingNumber());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(auditMapper, times(1)).toDto(any(ScaffoldingLogPositionDimension.class));
    }

    @Test
    void testFindByFilterAndPageEmpty() {
        ScaffoldingLogPositionDimensionFilter filter = new ScaffoldingLogPositionDimensionFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<ScaffoldingLogPositionDimension> dimensionPage = Page.empty();
        when(dao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(dimensionPage);

        Page<ScaffoldingLogPositionDimensionAuditableDTO> result = dimensionQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(auditMapper, never()).toDto(any(ScaffoldingLogPositionDimension.class));
    }

    @Test
    void testFindByFilterWithCriteria() {
        ScaffoldingLogPositionDimensionFilter filter = ScaffoldingLogPositionDimensionFilter.builder()
                .heightGreaterOrEqual(new BigDecimal("5.0"))
                .build();

        when(dao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(dimension));
        when(auditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(dimensionAuditableDTO));

        List<ScaffoldingLogPositionDimensionAuditableDTO> result = dimensionQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class));
        verify(auditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageWithCriteria() {
        ScaffoldingLogPositionDimensionFilter filter = ScaffoldingLogPositionDimensionFilter.builder()
                .heightGreaterOrEqual(new BigDecimal("5.0"))
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<ScaffoldingLogPositionDimension> dimensionPage = new PageImpl<>(Collections.singletonList(dimension), pageable, 1);
        when(dao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(dimensionPage);
        when(auditMapper.toDto(any(ScaffoldingLogPositionDimension.class))).thenReturn(dimensionAuditableDTO);

        Page<ScaffoldingLogPositionDimensionAuditableDTO> result = dimensionQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(auditMapper, times(1)).toDto(any(ScaffoldingLogPositionDimension.class));
    }
}
