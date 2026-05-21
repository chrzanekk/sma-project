package pl.com.chrzanowski.sma.unitTests.scaffolding.log.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.common.exception.ScaffoldingLogException;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.log.dao.ScaffoldingLogDao;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogDTO;
import pl.com.chrzanowski.sma.scaffolding.log.mapper.ScaffoldingLogDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;
import pl.com.chrzanowski.sma.scaffolding.log.service.ScaffoldingLogServiceImpl;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ScaffoldingLogServiceImplTest {

    @Mock
    private ScaffoldingLogDao scaffoldingLogDao;

    @Mock
    private ScaffoldingLogDTOMapper scaffoldingLogDTOMapper;

    @InjectMocks
    private ScaffoldingLogServiceImpl service;

    private ScaffoldingLogDTO scaffoldingLogDTO;
    private ScaffoldingLog scaffoldingLog;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        CompanyBaseDTO companyDTO = CompanyBaseDTO.builder()
                .id(1L)
                .name("Test Company")
                .additionalInfo("Additional info")
                .build();

        ConstructionSiteBaseDTO constructionSiteDTO = ConstructionSiteBaseDTO.builder()
                .id(1L)
                .name("Main Construction Site")
                .address("456 Builder Street")
                .country(Country.POLAND)
                .shortName("MCS")
                .code("MCS-2025")
                .build();

        ContractorBaseDTO contractorDTO = ContractorBaseDTO.builder()
                .id(1L)
                .name("Primary Contractor")
                .taxNumber("9876543210")
                .street("Industrial Road")
                .buildingNo("88")
                .apartmentNo(null)
                .postalCode("02-456")
                .city("Krakow")
                .country(Country.POLAND)
                .customer(true)
                .supplier(true)
                .scaffoldingUser(true)
                .build();

        scaffoldingLogDTO = ScaffoldingLogDTO.builder()
                .id(1L)
                .name("Project Scaffolding Log")
                .additionalInfo("Comprehensive scaffolding documentation")
                .company(companyDTO)
                .constructionSite(constructionSiteDTO)
                .contractor(contractorDTO)
                .positions(Collections.emptySet())
                .build();

        scaffoldingLog = new ScaffoldingLog();
        scaffoldingLog.setId(1L);
        scaffoldingLog.setName("Project Scaffolding Log");
        scaffoldingLog.setAdditionalInfo("Comprehensive scaffolding documentation");
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveScaffoldingLog() {
        when(scaffoldingLogDTOMapper.toEntity(any(ScaffoldingLogDTO.class))).thenReturn(scaffoldingLog);
        when(scaffoldingLogDao.save(any(ScaffoldingLog.class))).thenReturn(scaffoldingLog);
        when(scaffoldingLogDTOMapper.toDto(any(ScaffoldingLog.class))).thenReturn(scaffoldingLogDTO);

        ScaffoldingLogDTO result = service.save(scaffoldingLogDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Project Scaffolding Log", result.getName());
        assertEquals("Comprehensive scaffolding documentation", result.getAdditionalInfo());
        assertEquals(1L, result.getCompany().getId());
        assertEquals(1L, result.getConstructionSite().getId());
        assertEquals(1L, result.getContractor().getId());

        verify(scaffoldingLogDao, times(1)).save(any(ScaffoldingLog.class));
        verify(scaffoldingLogDTOMapper, times(1)).toDto(any(ScaffoldingLog.class));
    }

    @Test
    void testSaveScaffoldingLogFailure() {
        when(scaffoldingLogDTOMapper.toEntity(any(ScaffoldingLogDTO.class))).thenReturn(scaffoldingLog);
        when(scaffoldingLogDao.save(any(ScaffoldingLog.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> service.save(scaffoldingLogDTO));
        verify(scaffoldingLogDao, times(1)).save(any(ScaffoldingLog.class));
    }

    @Test
    void testFindById() {
        when(scaffoldingLogDao.findById(1L)).thenReturn(Optional.of(scaffoldingLog));
        when(scaffoldingLogDTOMapper.toDto(any(ScaffoldingLog.class))).thenReturn(scaffoldingLogDTO);

        ScaffoldingLogDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Project Scaffolding Log", result.getName());
        assertEquals("Main Construction Site", result.getConstructionSite().getName());

        verify(scaffoldingLogDao, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(scaffoldingLogDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ScaffoldingLogException.class, () -> service.findById(1L));
        verify(scaffoldingLogDao, times(1)).findById(1L);
    }

    @Test
    void testUpdateScaffoldingLog() {
        when(scaffoldingLogDao.findById(anyLong())).thenReturn(Optional.of(scaffoldingLog));
        doNothing().when(scaffoldingLogDTOMapper).updateFromDto(any(ScaffoldingLogDTO.class), any(ScaffoldingLog.class));
        when(scaffoldingLogDao.save(any(ScaffoldingLog.class))).thenReturn(scaffoldingLog);
        when(scaffoldingLogDTOMapper.toDto(any(ScaffoldingLog.class))).thenReturn(scaffoldingLogDTO);

        ScaffoldingLogDTO result = service.update(scaffoldingLogDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Project Scaffolding Log", result.getName());

        verify(scaffoldingLogDao, times(1)).findById(anyLong());
        verify(scaffoldingLogDao, times(1)).save(any(ScaffoldingLog.class));
        verify(scaffoldingLogDTOMapper, times(1)).updateFromDto(any(ScaffoldingLogDTO.class), any(ScaffoldingLog.class));
    }

    @Test
    void testUpdateScaffoldingLogNotFound() {
        when(scaffoldingLogDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ScaffoldingLogException.class, () -> service.update(scaffoldingLogDTO));
        verify(scaffoldingLogDao, times(1)).findById(anyLong());
        verify(scaffoldingLogDao, times(0)).save(any(ScaffoldingLog.class));
    }

    @Test
    void testUpdateScaffoldingLogFailure() {
        when(scaffoldingLogDao.findById(anyLong())).thenReturn(Optional.of(scaffoldingLog));
        doNothing().when(scaffoldingLogDTOMapper).updateFromDto(any(ScaffoldingLogDTO.class), any(ScaffoldingLog.class));
        when(scaffoldingLogDao.save(any(ScaffoldingLog.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> service.update(scaffoldingLogDTO));
        verify(scaffoldingLogDao, times(1)).findById(anyLong());
        verify(scaffoldingLogDao, times(1)).save(any(ScaffoldingLog.class));
    }

    @Test
    void testDeleteScaffoldingLog() {
        Long id = 1L;
        when(scaffoldingLogDao.findById(id)).thenReturn(Optional.of(scaffoldingLog));
        doNothing().when(scaffoldingLogDao).deleteById(id);

        service.delete(id);

        verify(scaffoldingLogDao, times(1)).findById(id);
        verify(scaffoldingLogDao, times(1)).deleteById(id);
    }

    @Test
    void testDeleteScaffoldingLogNotFound() {
        Long id = 1L;
        when(scaffoldingLogDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(ScaffoldingLogException.class, () -> service.delete(id));
        verify(scaffoldingLogDao, times(1)).findById(id);
        verify(scaffoldingLogDao, times(0)).deleteById(id);
    }

    @Test
    void testDeleteScaffoldingLogFailure() {
        Long id = 1L;
        when(scaffoldingLogDao.findById(id)).thenReturn(Optional.of(scaffoldingLog));
        doThrow(new RuntimeException("Database error")).when(scaffoldingLogDao).deleteById(id);

        assertThrows(RuntimeException.class, () -> service.delete(id));
        verify(scaffoldingLogDao, times(1)).findById(id);
        verify(scaffoldingLogDao, times(1)).deleteById(id);
    }
}
