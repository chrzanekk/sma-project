package pl.com.chrzanowski.sma.unitTests.scaffolding.workingtime.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingOperationType;
import pl.com.chrzanowski.sma.common.exception.ScaffoldingLogPositionWorkingTimeException;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dao.ScaffoldingLogPositionWorkingTimeDao;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.mapper.ScaffoldingLogPositionWorkingTimeDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;
import pl.com.chrzanowski.sma.scaffolding.workingtime.service.ScaffoldingLogPositionWorkingTimeServiceImpl;


import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ScaffoldingLogPositionWorkingTimeServiceImplTest {

    @Mock
    private ScaffoldingLogPositionWorkingTimeDao dao;

    @Mock
    private ScaffoldingLogPositionWorkingTimeDTOMapper dtoMapper;

    @InjectMocks
    private ScaffoldingLogPositionWorkingTimeServiceImpl service;

    private ScaffoldingLogPositionWorkingTimeDTO workingTimeDTO;
    private ScaffoldingLogPositionWorkingTime workingTime;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        CompanyBaseDTO companyDTO = CompanyBaseDTO.builder()
                .id(1L)
                .name("Test Company")
                .additionalInfo("Additional info")
                .build();

        workingTimeDTO = ScaffoldingLogPositionWorkingTimeDTO.builder()
                .id(1L)
                .numberOfWorkers(new BigDecimal("5"))
                .numberOfHours(new BigDecimal("8.5"))
                .operationType(ScaffoldingOperationType.ASSEMBLY)
                .company(companyDTO)
                .build();

        workingTime = new ScaffoldingLogPositionWorkingTime();
        workingTime.setId(1L);
        workingTime.setNumberOfWorkers(new BigDecimal("5"));
        workingTime.setNumberOfHours(new BigDecimal("8.5"));
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveWorkingTime() {
        when(dtoMapper.toEntity(any(ScaffoldingLogPositionWorkingTimeDTO.class))).thenReturn(workingTime);
        when(dao.save(any(ScaffoldingLogPositionWorkingTime.class))).thenReturn(workingTime);
        when(dtoMapper.toDto(any(ScaffoldingLogPositionWorkingTime.class))).thenReturn(workingTimeDTO);

        ScaffoldingLogPositionWorkingTimeDTO result = service.save(workingTimeDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("5"), result.getNumberOfWorkers());
        assertEquals(new BigDecimal("8.5"), result.getNumberOfHours());
        assertEquals(ScaffoldingOperationType.ASSEMBLY, result.getOperationType());
        assertEquals(1L, result.getCompany().getId());

        verify(dao, times(1)).save(any(ScaffoldingLogPositionWorkingTime.class));
        verify(dtoMapper, times(1)).toDto(any(ScaffoldingLogPositionWorkingTime.class));
    }

    @Test
    void testSaveWorkingTimeFailure() {
        when(dtoMapper.toEntity(any(ScaffoldingLogPositionWorkingTimeDTO.class))).thenReturn(workingTime);
        when(dao.save(any(ScaffoldingLogPositionWorkingTime.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> service.save(workingTimeDTO));
        verify(dao, times(1)).save(any(ScaffoldingLogPositionWorkingTime.class));
    }

    @Test
    void testFindById() {
        when(dao.findById(1L)).thenReturn(Optional.of(workingTime));
        when(dtoMapper.toDto(any(ScaffoldingLogPositionWorkingTime.class))).thenReturn(workingTimeDTO);

        ScaffoldingLogPositionWorkingTimeDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("5"), result.getNumberOfWorkers());
        assertEquals(new BigDecimal("8.5"), result.getNumberOfHours());

        verify(dao, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(dao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ScaffoldingLogPositionWorkingTimeException.class, () -> service.findById(1L));
        verify(dao, times(1)).findById(1L);
    }

    @Test
    void testUpdateWorkingTime() {
        when(dao.findById(anyLong())).thenReturn(Optional.of(workingTime));
        doNothing().when(dtoMapper).updateFromDto(any(ScaffoldingLogPositionWorkingTimeDTO.class), any(ScaffoldingLogPositionWorkingTime.class));
        when(dao.save(any(ScaffoldingLogPositionWorkingTime.class))).thenReturn(workingTime);
        when(dtoMapper.toDto(any(ScaffoldingLogPositionWorkingTime.class))).thenReturn(workingTimeDTO);

        ScaffoldingLogPositionWorkingTimeDTO result = service.update(workingTimeDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("5"), result.getNumberOfWorkers());
        assertEquals(new BigDecimal("8.5"), result.getNumberOfHours());

        verify(dao, times(1)).findById(anyLong());
        verify(dao, times(1)).save(any(ScaffoldingLogPositionWorkingTime.class));
        verify(dtoMapper, times(1)).updateFromDto(any(ScaffoldingLogPositionWorkingTimeDTO.class), any(ScaffoldingLogPositionWorkingTime.class));
    }

    @Test
    void testUpdateWorkingTimeNotFound() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ScaffoldingLogPositionWorkingTimeException.class, () -> service.update(workingTimeDTO));
        verify(dao, times(1)).findById(anyLong());
        verify(dao, times(0)).save(any(ScaffoldingLogPositionWorkingTime.class));
    }

    @Test
    void testUpdateWorkingTimeFailure() {
        when(dao.findById(anyLong())).thenReturn(Optional.of(workingTime));
        doNothing().when(dtoMapper).updateFromDto(any(ScaffoldingLogPositionWorkingTimeDTO.class), any(ScaffoldingLogPositionWorkingTime.class));
        when(dao.save(any(ScaffoldingLogPositionWorkingTime.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> service.update(workingTimeDTO));
        verify(dao, times(1)).findById(anyLong());
        verify(dao, times(1)).save(any(ScaffoldingLogPositionWorkingTime.class));
    }

    @Test
    void testDeleteWorkingTime() {
        Long id = 1L;
        when(dao.findById(id)).thenReturn(Optional.of(workingTime));
        doNothing().when(dao).deleteById(id);

        service.delete(id);

        verify(dao, times(1)).findById(id);
        verify(dao, times(1)).deleteById(id);
    }

    @Test
    void testDeleteWorkingTimeNotFound() {
        Long id = 1L;
        when(dao.findById(id)).thenReturn(Optional.empty());

        assertThrows(ScaffoldingLogPositionWorkingTimeException.class, () -> service.delete(id));
        verify(dao, times(1)).findById(id);
        verify(dao, times(0)).deleteById(id);
    }

    @Test
    void testDeleteWorkingTimeFailure() {
        Long id = 1L;
        when(dao.findById(id)).thenReturn(Optional.of(workingTime));
        doThrow(new RuntimeException("Database error")).when(dao).deleteById(id);

        assertThrows(RuntimeException.class, () -> service.delete(id));
        verify(dao, times(1)).findById(id);
        verify(dao, times(1)).deleteById(id);
    }
}
