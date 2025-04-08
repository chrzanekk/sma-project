package pl.com.chrzanowski.sma.unitTests.company.service;

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
import pl.com.chrzanowski.sma.common.exception.CompanyException;
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.dto.CompanyAuditableDTO;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyAuditMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyDTOMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.service.CompanyQueryServiceImpl;
import pl.com.chrzanowski.sma.company.service.filter.CompanyFilter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class CompanyQueryServiceImplTest {

    @Mock
    private CompanyDao companyDao;

    @Mock
    private CompanyAuditMapper companyAuditMapper;

    @Mock
    private CompanyDTOMapper companyDTOMapper;

    @InjectMocks
    private CompanyQueryServiceImpl companyQueryService;

    private CompanyDTO companyDTO;
    private CompanyAuditableDTO companyAuditableDTO;
    private Company company;
    private AutoCloseable autoCloseable;
    private String companyName;


    @BeforeEach
    void setUp() {
        companyName = "Test company";
        autoCloseable = MockitoAnnotations.openMocks(this);

        companyDTO = CompanyDTO.builder()
                .id(1L)
                .name(companyName)
                .build();

        companyAuditableDTO = CompanyAuditableDTO.builder()
                .base(companyDTO).build();

        company = new Company();
        company.setId(1L);
        company.setName(companyName);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testFindByFilterSuccess() {
        CompanyFilter filter = new CompanyFilter();

        when(companyDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(company));
        when(companyAuditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(companyAuditableDTO));

        List<CompanyAuditableDTO> result = companyQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(companyDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(companyAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterEmpty() {
        CompanyFilter filter = new CompanyFilter();

        when(companyDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(companyAuditMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<CompanyAuditableDTO> result = companyQueryService.findByFilter(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(companyDao, times(1)).findAll(any(BooleanBuilder.class));
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        CompanyFilter filter = new CompanyFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Company> companyPage = new PageImpl<>(Collections.singletonList(company));
        when(companyDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(companyPage);
        when(companyAuditMapper.toDto(any(Company.class))).thenReturn(companyAuditableDTO);

        Page<CompanyAuditableDTO> result = companyQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(companyDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(companyAuditMapper, times(1)).toDto(any(Company.class));
    }

    @Test
    void testFindByFilterAndPageEmpty() {
        CompanyFilter filter = new CompanyFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Company> companyPage = Page.empty();
        when(companyDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(companyPage);

        Page<CompanyAuditableDTO> result = companyQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(companyDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
    }

    @Test
    void testFindByName() {
        when(companyDao.findByName(companyName)).thenReturn(Optional.of(company));
        when(companyDTOMapper.toDto(any(Company.class))).thenReturn(companyDTO);

        CompanyDTO result = companyQueryService.findByName(companyName);
        assertNotNull(result);
        assertEquals(companyName, result.getName());

        verify(companyDao, times(1)).findByName(companyName);
    }

    @Test
    void testFindByNameNotFound() {
        when(companyDao.findByName("NotExistingCompany")).thenReturn(Optional.empty());

        assertThrows(CompanyException.class, () -> companyQueryService.findByName("NotExistingCompany"));
        verify(companyDao, times(1)).findByName("NotExistingCompany");
    }
}
