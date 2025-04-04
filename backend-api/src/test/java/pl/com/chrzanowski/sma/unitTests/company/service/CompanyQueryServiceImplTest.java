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
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.service.CompanyQueryServiceImpl;
import pl.com.chrzanowski.sma.company.service.filter.CompanyFilter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class CompanyQueryServiceImplTest {

    @Mock
    private CompanyDao companyDao;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyQueryServiceImpl companyQueryService;

    private CompanyBaseDTO companyBaseDTO;
    private Company company;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        companyBaseDTO = CompanyBaseDTO.builder()
                .id(1L)
                .name("Test Company")
                .build();

        company = new Company();
        company.setId(1L);
        company.setName("Test Company");
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testFindByFilterSuccess() {
        CompanyFilter filter = new CompanyFilter();

        when(companyDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(company));
        when(companyMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(companyBaseDTO));

        List<CompanyBaseDTO> result = companyQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(companyDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(companyMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterEmpty() {
        CompanyFilter filter = new CompanyFilter();

        when(companyDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(companyMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<CompanyBaseDTO> result = companyQueryService.findByFilter(filter);

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
        when(companyMapper.toDto(any(Company.class))).thenReturn(companyBaseDTO);

        Page<CompanyBaseDTO> result = companyQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(companyDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(companyMapper, times(1)).toDto(any(Company.class));
    }

    @Test
    void testFindByFilterAndPageEmpty() {
        CompanyFilter filter = new CompanyFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Company> companyPage = Page.empty();
        when(companyDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(companyPage);

        Page<CompanyBaseDTO> result = companyQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(companyDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
    }
}
