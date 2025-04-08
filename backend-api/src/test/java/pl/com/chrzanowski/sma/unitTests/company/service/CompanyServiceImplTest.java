package pl.com.chrzanowski.sma.unitTests.company.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.exception.CompanyException;
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyDTOMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.service.CompanyServiceImpl;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CompanyServiceImplTest {

    @Mock
    private CompanyDao companyDao;

    @Mock
    private CompanyDTOMapper companyDTOMapper;

    @InjectMocks
    private CompanyServiceImpl companyServiceImpl;

    private CompanyDTO companyDTO;
    private Company company;
    private AutoCloseable autoCloseable;
    private String companyName;
    private String additionalInfo;

    @BeforeEach
    void setUp() throws Exception {
        companyName = "Test company";
        additionalInfo = "Test additional info";
        autoCloseable = MockitoAnnotations.openMocks(this);

        companyDTO = CompanyDTO.builder()
                .id(1L)
                .name(companyName)
                .additionalInfo(additionalInfo)
                .build();

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
        when(companyDTOMapper.toEntity(any(CompanyDTO.class))).thenReturn(company);
        when(companyDao.save(any(Company.class))).thenReturn(company);
        when(companyDTOMapper.toDto(any(Company.class))).thenReturn(companyDTO);

        CompanyDTO result = companyServiceImpl.save(companyDTO);
        assertNotNull(result);
        assertEquals(companyName, result.getName());
        assertEquals(additionalInfo, result.getAdditionalInfo());

        verify(companyDao, times(1)).save(any(Company.class));
        verify(companyDTOMapper, times(1)).toDto(any(Company.class));
    }

    @Test
    void testSaveCompany_Failure() {
        when(companyDTOMapper.toEntity(any(CompanyDTO.class))).thenReturn(company);
        when(companyDao.save(any(Company.class))).thenThrow(new RuntimeException("Error"));

        assertThrows(RuntimeException.class, () -> companyServiceImpl.save(companyDTO));
        verify(companyDao, times(1)).save(any(Company.class));
    }

    @Test
    void testUpdateCompany() {
        Company updatedCompany = company;
        updatedCompany.setName("Updated company");
        CompanyDTO updatedCompanyDTO = CompanyDTO.builder()
                .id(companyDTO.getId())
                .name(updatedCompany.getName())
                .additionalInfo(updatedCompany.getAdditionalInfo())
                .build();
        when(companyDao.findById(anyLong())).thenReturn(Optional.of(company));
        when(companyDTOMapper.toDto(any(Company.class))).thenReturn(updatedCompanyDTO);
        when(companyDao.save(any(Company.class))).thenReturn(updatedCompany);

        CompanyDTO result = companyServiceImpl.update(companyDTO);
        assertNotNull(result);
        assertEquals(updatedCompany.getName(), result.getName());
        assertEquals(updatedCompany.getAdditionalInfo(), result.getAdditionalInfo());

        verify(companyDao, times(1)).findById(anyLong());
        verify(companyDTOMapper, times(1)).toDto(any(Company.class));
        verify(companyDao, times(1)).save(any(Company.class));
    }

    @Test
    void testUpdateCompany_Failure() {
        when(companyDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CompanyException.class, () -> companyServiceImpl.update(companyDTO));
        verify(companyDao, times(1)).findById(anyLong());
    }

    @Test
    void testFindById() {
        when(companyDao.findById(1L)).thenReturn(Optional.of(company));
        when(companyDTOMapper.toDto(any(Company.class))).thenReturn(companyDTO);

        CompanyDTO result = companyServiceImpl.findById(1L);
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
    void testDeleteCompany() {
        companyServiceImpl.delete(companyDTO.getId());
        verify(companyDao, times(1)).deleteById(companyDTO.getId());
    }
}
