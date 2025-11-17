package pl.com.chrzanowski.sma.unitTests.scaffolding.workingtime.service;

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
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingOperationType;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingType;
import pl.com.chrzanowski.sma.common.enumeration.TechnicalProtocolStatus;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dao.ScaffoldingLogPositionWorkingTimeDao;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.mapper.ScaffoldingLogPositionWorkingTimeAuditMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;
import pl.com.chrzanowski.sma.scaffolding.workingtime.service.ScaffoldingLogPositionWorkingTimeQueryServiceImpl;
import pl.com.chrzanowski.sma.scaffolding.workingtime.service.filter.ScaffoldingLogPositionWorkingTimeFilter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ScaffoldingLogPositionWorkingTimeQueryServiceImplTest {

    @Mock
    private ScaffoldingLogPositionWorkingTimeDao dao;

    @Mock
    private ScaffoldingLogPositionWorkingTimeAuditMapper auditMapper;

    @InjectMocks
    private ScaffoldingLogPositionWorkingTimeQueryServiceImpl workingTimeQueryService;

    private ScaffoldingLogPositionWorkingTimeDTO workingTimeDTO;
    private ScaffoldingLogPositionWorkingTimeAuditableDTO workingTimeAuditableDTO;
    private ScaffoldingLogPositionWorkingTime workingTime;
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
                .build();

        // Tworzenie ScaffoldingLogPositionWorkingTimeDTO
        workingTimeDTO = ScaffoldingLogPositionWorkingTimeDTO.builder()
                .id(1L)
                .numberOfWorkers(5)
                .numberOfHours(new BigDecimal("8.0"))
                .scaffoldingPosition(scaffoldingPosition)
                .operationType(ScaffoldingOperationType.ASSEMBLY)
                .company(company)
                .build();

        workingTimeAuditableDTO = ScaffoldingLogPositionWorkingTimeAuditableDTO.builder()
                .base(workingTimeDTO)
                .build();

        // Tworzenie encji ScaffoldingLogPositionWorkingTime
        workingTime = new ScaffoldingLogPositionWorkingTime();
        workingTime.setId(1L);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testFindByFilterSuccess() {
        ScaffoldingLogPositionWorkingTimeFilter filter = new ScaffoldingLogPositionWorkingTimeFilter();

        when(dao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(workingTime));
        when(auditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(workingTimeAuditableDTO));

        List<ScaffoldingLogPositionWorkingTimeAuditableDTO> result = workingTimeQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0, new BigDecimal("8.0").compareTo(result.getFirst().getBase().getNumberOfHours()));
        assertEquals(ScaffoldingOperationType.ASSEMBLY, result.getFirst().getBase().getOperationType());
        assertEquals("Test Company", result.getFirst().getBase().getCompany().getName());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class));
        verify(auditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterEmpty() {
        ScaffoldingLogPositionWorkingTimeFilter filter = new ScaffoldingLogPositionWorkingTimeFilter();

        when(dao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(auditMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<ScaffoldingLogPositionWorkingTimeAuditableDTO> result = workingTimeQueryService.findByFilter(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class));
        verify(auditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        ScaffoldingLogPositionWorkingTimeFilter filter = new ScaffoldingLogPositionWorkingTimeFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<ScaffoldingLogPositionWorkingTime> workingTimePage = new PageImpl<>(Collections.singletonList(workingTime));
        when(dao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(workingTimePage);
        when(auditMapper.toDto(any(ScaffoldingLogPositionWorkingTime.class))).thenReturn(workingTimeAuditableDTO);

        Page<ScaffoldingLogPositionWorkingTimeAuditableDTO> result = workingTimeQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(0, new BigDecimal("8.0").compareTo(result.getContent().getFirst().getBase().getNumberOfHours()));
        assertEquals("SC-001", result.getContent().getFirst().getBase().getScaffoldingPosition().getScaffoldingNumber());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(auditMapper, times(1)).toDto(any(ScaffoldingLogPositionWorkingTime.class));
    }

    @Test
    void testFindByFilterAndPageEmpty() {
        ScaffoldingLogPositionWorkingTimeFilter filter = new ScaffoldingLogPositionWorkingTimeFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<ScaffoldingLogPositionWorkingTime> workingTimePage = Page.empty();
        when(dao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(workingTimePage);

        Page<ScaffoldingLogPositionWorkingTimeAuditableDTO> result = workingTimeQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(auditMapper, never()).toDto(any(ScaffoldingLogPositionWorkingTime.class));
    }

    @Test
    void testFindByFilterWithCriteria() {
        ScaffoldingLogPositionWorkingTimeFilter filter = ScaffoldingLogPositionWorkingTimeFilter.builder()
                .numberOfWorkersGreaterOrEqual(new BigDecimal("3"))
                .build();

        when(dao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(workingTime));
        when(auditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(workingTimeAuditableDTO));

        List<ScaffoldingLogPositionWorkingTimeAuditableDTO> result = workingTimeQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class));
        verify(auditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageWithCriteria() {
        ScaffoldingLogPositionWorkingTimeFilter filter = ScaffoldingLogPositionWorkingTimeFilter.builder()
                .numberOfWorkersGreaterOrEqual(new BigDecimal("3"))
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<ScaffoldingLogPositionWorkingTime> workingTimePage = new PageImpl<>(Collections.singletonList(workingTime), pageable, 1);
        when(dao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(workingTimePage);
        when(auditMapper.toDto(any(ScaffoldingLogPositionWorkingTime.class))).thenReturn(workingTimeAuditableDTO);

        Page<ScaffoldingLogPositionWorkingTimeAuditableDTO> result = workingTimeQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        verify(dao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(auditMapper, times(1)).toDto(any(ScaffoldingLogPositionWorkingTime.class));
    }
}
