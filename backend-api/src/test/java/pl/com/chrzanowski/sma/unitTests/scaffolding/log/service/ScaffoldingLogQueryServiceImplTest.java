package pl.com.chrzanowski.sma.unitTests.scaffolding.log.service;

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
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.log.dao.ScaffoldingLogDao;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogDTO;
import pl.com.chrzanowski.sma.scaffolding.log.mapper.ScaffoldingLogAuditMapper;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;
import pl.com.chrzanowski.sma.scaffolding.log.service.ScaffoldingLogQueryServiceImpl;
import pl.com.chrzanowski.sma.scaffolding.log.service.filter.ScaffoldingLogFilter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ScaffoldingLogQueryServiceImplTest {

    @Mock
    private ScaffoldingLogDao scaffoldingLogDao;

    @Mock
    private ScaffoldingLogAuditMapper scaffoldingLogAuditMapper;

    @InjectMocks
    private ScaffoldingLogQueryServiceImpl scaffoldingLogQueryService;

    private ScaffoldingLogDTO scaffoldingLogDTO;
    private ScaffoldingLogAuditableDTO scaffoldingLogAuditableDTO;
    private ScaffoldingLog scaffoldingLog;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        // Tworzenie CompanyBaseDTO
        CompanyBaseDTO company = CompanyBaseDTO.builder()
                .id(1L)
                .name("Test Company")
                .additionalInfo("Company info")
                .build();

        // Tworzenie ConstructionSiteBaseDTO
        ConstructionSiteBaseDTO constructionSite = ConstructionSiteBaseDTO.builder()
                .id(1L)
                .name("Construction Site Alpha")
                .address("123 Main Street")
                .country(Country.POLAND)
                .shortName("CS-Alpha")
                .code("CSA-001")
                .build();

        // Tworzenie ContractorBaseDTO
        ContractorBaseDTO contractor = ContractorBaseDTO.builder()
                .id(1L)
                .name("Contractor XYZ")
                .taxNumber("1234567890")
                .street("Oak Avenue")
                .buildingNo("15")
                .apartmentNo("2A")
                .postalCode("00-001")
                .city("Warsaw")
                .country(Country.POLAND)
                .customer(true)
                .supplier(false)
                .scaffoldingUser(true)
                .build();

        // Tworzenie ScaffoldingLogDTO
        scaffoldingLogDTO = ScaffoldingLogDTO.builder()
                .id(1L)
                .name("Scaffolding Log 2025")
                .additionalInfo("Main scaffolding operations")
                .company(company)
                .constructionSite(constructionSite)
                .contractor(contractor)
                .positions(Collections.emptySet())
                .build();

        scaffoldingLogAuditableDTO = ScaffoldingLogAuditableDTO.builder()
                .base(scaffoldingLogDTO)
                .build();

        // Tworzenie encji ScaffoldingLog
        scaffoldingLog = new ScaffoldingLog();
        scaffoldingLog.setId(1L);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testFindByFilterSuccess() {
        ScaffoldingLogFilter filter = new ScaffoldingLogFilter();

        when(scaffoldingLogDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(scaffoldingLog));
        when(scaffoldingLogAuditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(scaffoldingLogAuditableDTO));

        List<ScaffoldingLogAuditableDTO> result = scaffoldingLogQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Scaffolding Log 2025", result.getFirst().getBase().getName());
        assertEquals("Test Company", result.getFirst().getBase().getCompany().getName());
        assertEquals("Construction Site Alpha", result.getFirst().getBase().getConstructionSite().getName());
        assertEquals("Contractor XYZ", result.getFirst().getBase().getContractor().getName());

        verify(scaffoldingLogDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(scaffoldingLogAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterEmpty() {
        ScaffoldingLogFilter filter = new ScaffoldingLogFilter();

        when(scaffoldingLogDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(scaffoldingLogAuditMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<ScaffoldingLogAuditableDTO> result = scaffoldingLogQueryService.findByFilter(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(scaffoldingLogDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(scaffoldingLogAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        ScaffoldingLogFilter filter = new ScaffoldingLogFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<ScaffoldingLog> scaffoldingLogPage = new PageImpl<>(Collections.singletonList(scaffoldingLog));
        when(scaffoldingLogDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(scaffoldingLogPage);
        when(scaffoldingLogAuditMapper.toDto(any(ScaffoldingLog.class))).thenReturn(scaffoldingLogAuditableDTO);

        Page<ScaffoldingLogAuditableDTO> result = scaffoldingLogQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Scaffolding Log 2025", result.getContent().getFirst().getBase().getName());
        assertEquals("CSA-001", result.getContent().getFirst().getBase().getConstructionSite().getCode());

        verify(scaffoldingLogDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(scaffoldingLogAuditMapper, times(1)).toDto(any(ScaffoldingLog.class));
    }

    @Test
    void testFindByFilterAndPageEmpty() {
        ScaffoldingLogFilter filter = new ScaffoldingLogFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<ScaffoldingLog> scaffoldingLogPage = Page.empty();
        when(scaffoldingLogDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(scaffoldingLogPage);

        Page<ScaffoldingLogAuditableDTO> result = scaffoldingLogQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(scaffoldingLogDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(scaffoldingLogAuditMapper, never()).toDto(any(ScaffoldingLog.class));
    }

    @Test
    void testFindByFilterWithCriteria() {
        ScaffoldingLogFilter filter = ScaffoldingLogFilter.builder()
                .nameContains("2025")
                .companyId(1L)
                .build();

        when(scaffoldingLogDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(scaffoldingLog));
        when(scaffoldingLogAuditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(scaffoldingLogAuditableDTO));

        List<ScaffoldingLogAuditableDTO> result = scaffoldingLogQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(scaffoldingLogDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(scaffoldingLogAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageWithCriteria() {
        ScaffoldingLogFilter filter = ScaffoldingLogFilter.builder()
                .constructionSiteId(1L)
                .contractorId(1L)
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<ScaffoldingLog> scaffoldingLogPage = new PageImpl<>(Collections.singletonList(scaffoldingLog), pageable, 1);
        when(scaffoldingLogDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(scaffoldingLogPage);
        when(scaffoldingLogAuditMapper.toDto(any(ScaffoldingLog.class))).thenReturn(scaffoldingLogAuditableDTO);

        Page<ScaffoldingLogAuditableDTO> result = scaffoldingLogQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        verify(scaffoldingLogDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(scaffoldingLogAuditMapper, times(1)).toDto(any(ScaffoldingLog.class));
    }
}
