package pl.com.chrzanowski.sma.unitTests.company.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.exception.CompanyException;
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.service.CompanyServiceImpl;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CompanyServiceImplTest {

    @Mock
    private CompanyDao companyDao;

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CompanyServiceImpl companyServiceImpl;

    private CompanyBaseDTO companyBaseDTO;
    private Company company;
    private AutoCloseable autoCloseable;
    private String companyName;
    private String additionalInfo;

    @BeforeEach
    void setUp() throws Exception {
        companyName = "Test company";
        additionalInfo = "Test additional info";
        autoCloseable = MockitoAnnotations.openMocks(this);

        companyBaseDTO = CompanyBaseDTO.builder()
                .id(1L)
                .name(companyName)
                .additionalInfo(additionalInfo)
                .createdDatetime(Instant.now()).build();

        company = new Company();
        company.setId(1L);
        company.setName(companyName);
        company.setAdditionalInfo(additionalInfo);
        company.setCreatedDatetime(Instant.now());
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveCompany() {
        when(companyMapper.toEntity(any(CompanyBaseDTO.class))).thenReturn(company);
        when(companyDao.save(any(Company.class))).thenReturn(company);
        when(companyMapper.toDto(any(Company.class))).thenReturn(companyBaseDTO);

        CompanyBaseDTO result = companyServiceImpl.save(companyBaseDTO);
        assertNotNull(result);
        assertEquals(companyName, result.getName());
        assertEquals(additionalInfo, result.getAdditionalInfo());

        verify(companyDao, times(1)).save(any(Company.class));
        verify(companyMapper, times(1)).toDto(any(Company.class));
    }

    @Test
    void testSaveCompany_Failure() {
        when(companyMapper.toEntity(any(CompanyBaseDTO.class))).thenReturn(company);
        when(companyDao.save(any(Company.class))).thenThrow(new RuntimeException("Error"));

        assertThrows(RuntimeException.class, () -> companyServiceImpl.save(companyBaseDTO));
        verify(companyDao, times(1)).save(any(Company.class));
    }

    @Test
    void testUpdateCompany() {
        Company updatedCompany = company;
        updatedCompany.setName("Updated company");
        CompanyBaseDTO updatedCompanyDTO = CompanyBaseDTO.builder()
                .id(companyBaseDTO.getId())
                .name(updatedCompany.getName())
                .additionalInfo(updatedCompany.getAdditionalInfo())
                .build();
        when(companyDao.findById(anyLong())).thenReturn(Optional.of(company));
        when(companyMapper.toDto(any(Company.class))).thenReturn(updatedCompanyDTO);
        when(companyDao.save(any(Company.class))).thenReturn(updatedCompany);

        CompanyBaseDTO result = companyServiceImpl.update(companyBaseDTO);
        assertNotNull(result);
        assertEquals(updatedCompany.getName(), result.getName());
        assertEquals(updatedCompany.getAdditionalInfo(), result.getAdditionalInfo());

        verify(companyDao, times(1)).findById(anyLong());
        verify(companyMapper, times(1)).toDto(any(Company.class));
        verify(companyDao, times(1)).save(any(Company.class));
    }

    @Test
    void testUpdateCompany_Failure() {
        when(companyDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CompanyException.class, () -> companyServiceImpl.update(companyBaseDTO));
        verify(companyDao, times(1)).findById(anyLong());
    }

    @Test
    void testFindById() {
        when(companyDao.findById(1L)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(any(Company.class))).thenReturn(companyBaseDTO);

        CompanyBaseDTO result = companyServiceImpl.findById(1L);
        assertNotNull(result);
        assertEquals(companyName, result.getName());

        verify(companyDao, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(companyDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CompanyException.class, () -> companyServiceImpl.findById(1L));
        verify(companyDao, times(1)).findById(1L);
    }

    @Test
    void testFindByName() {
        when(companyDao.findByName(companyName)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(any(Company.class))).thenReturn(companyBaseDTO);

        CompanyBaseDTO result = companyServiceImpl.findByName(companyName);
        assertNotNull(result);
        assertEquals(companyName, result.getName());

        verify(companyDao, times(1)).findByName(companyName);
    }

    @Test
    void testFindByNameNotFound() {
        when(companyDao.findByName("NotExistingCompany")).thenReturn(Optional.empty());

        assertThrows(CompanyException.class, () -> companyServiceImpl.findByName("NotExistingCompany"));
        verify(companyDao, times(1)).findByName("NotExistingCompany");
    }

    @Test
    void testFindAll() {
        when(companyDao.findAll()).thenReturn(Collections.singletonList(company));
        when(companyMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(companyBaseDTO));

        List<CompanyBaseDTO> result = companyServiceImpl.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(companyName, result.get(0).getName());
        assertTrue(result.contains(companyBaseDTO));

        verify(companyDao, times(1)).findAll();
    }

    @Test
    void testFindAllNotFound() {
        when(companyDao.findAll()).thenReturn(Collections.emptyList());
        when(companyMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<CompanyBaseDTO> result = companyServiceImpl.findAll();

        assertTrue(result.isEmpty());
        verify(companyDao, times(1)).findAll();
    }

    @Test
    void testDeleteCompany() {
        companyServiceImpl.delete(companyBaseDTO.getId());
        verify(companyDao, times(1)).deleteById(companyBaseDTO.getId());
    }
}
