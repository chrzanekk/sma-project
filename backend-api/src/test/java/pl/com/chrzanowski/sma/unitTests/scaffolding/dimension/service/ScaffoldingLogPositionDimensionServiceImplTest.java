package pl.com.chrzanowski.sma.unitTests.scaffolding.dimension.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.enumeration.DimensionType;
import pl.com.chrzanowski.sma.common.exception.ScaffoldingLogPositionDimensionException;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.dao.ScaffoldingLogPositionDimensionDao;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.mapper.ScaffoldingLogPositionDimensionDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;
import pl.com.chrzanowski.sma.scaffolding.dimension.service.ScaffoldingLogPositionDimensionServiceImpl;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeBaseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ScaffoldingLogPositionDimensionServiceImplTest {

    @Mock
    private ScaffoldingLogPositionDimensionDao dao;

    @Mock
    private ScaffoldingLogPositionDimensionDTOMapper dtoMapper;

    @InjectMocks
    private ScaffoldingLogPositionDimensionServiceImpl service;

    private ScaffoldingLogPositionDimensionDTO dimensionDTO;
    private ScaffoldingLogPositionDimension dimension;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        WorkTypeBaseDTO workTypeDTO = WorkTypeBaseDTO.builder()
                .id(1L)
                .name("Assembly")
                .description("Scaffolding assembly work")
                .build();

        CompanyBaseDTO companyDTO = CompanyBaseDTO.builder()
                .id(1L)
                .name("Test Company")
                .additionalInfo("Additional info")
                .build();

        dimensionDTO = ScaffoldingLogPositionDimensionDTO.builder()
                .id(1L)
                .height(new BigDecimal("12.5"))
                .width(new BigDecimal("6.0"))
                .length(new BigDecimal("18.0"))
                .dimensionType(DimensionType.CONSOLE)
                .dismantlingDate(LocalDate.of(2025, 7, 15))
                .workType(workTypeDTO)
                .company(companyDTO)
                .build();

        dimension = new ScaffoldingLogPositionDimension();
        dimension.setId(1L);
        dimension.setHeight(new BigDecimal("12.5"));
        dimension.setWidth(new BigDecimal("6.0"));
        dimension.setLength(new BigDecimal("18.0"));
        dimension.setDimensionType(DimensionType.CONSOLE);
        dimension.setDismantlingDate(LocalDate.of(2025, 7, 15));
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveDimension() {
        when(dtoMapper.toEntity(any(ScaffoldingLogPositionDimensionDTO.class))).thenReturn(dimension);
        when(dao.save(any(ScaffoldingLogPositionDimension.class))).thenReturn(dimension);
        when(dtoMapper.toDto(any(ScaffoldingLogPositionDimension.class))).thenReturn(dimensionDTO);

        ScaffoldingLogPositionDimensionDTO result = service.save(dimensionDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("12.5"), result.getHeight());
        assertEquals(new BigDecimal("6.0"), result.getWidth());
        assertEquals(new BigDecimal("18.0"), result.getLength());
        assertEquals(DimensionType.CONSOLE, result.getDimensionType());
        assertEquals(1L, result.getWorkType().getId());
        assertEquals(1L, result.getCompany().getId());

        verify(dao, times(1)).save(any(ScaffoldingLogPositionDimension.class));
        verify(dtoMapper, times(1)).toDto(any(ScaffoldingLogPositionDimension.class));
    }

    @Test
    void testSaveDimensionFailure() {
        when(dtoMapper.toEntity(any(ScaffoldingLogPositionDimensionDTO.class))).thenReturn(dimension);
        when(dao.save(any(ScaffoldingLogPositionDimension.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> service.save(dimensionDTO));
        verify(dao, times(1)).save(any(ScaffoldingLogPositionDimension.class));
    }

    @Test
    void testFindById() {
        when(dao.findById(1L)).thenReturn(Optional.of(dimension));
        when(dtoMapper.toDto(any(ScaffoldingLogPositionDimension.class))).thenReturn(dimensionDTO);

        ScaffoldingLogPositionDimensionDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("12.5"), result.getHeight());
        assertEquals(new BigDecimal("18.0"), result.getLength());
        assertEquals(DimensionType.CONSOLE, result.getDimensionType());

        verify(dao, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(dao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ScaffoldingLogPositionDimensionException.class, () -> service.findById(1L));
        verify(dao, times(1)).findById(1L);
    }

    @Test
    void testUpdateDimension() {
        when(dao.findById(anyLong())).thenReturn(Optional.of(dimension));
        doNothing().when(dtoMapper).updateFromDto(any(ScaffoldingLogPositionDimensionDTO.class), any(ScaffoldingLogPositionDimension.class));
        when(dao.save(any(ScaffoldingLogPositionDimension.class))).thenReturn(dimension);
        when(dtoMapper.toDto(any(ScaffoldingLogPositionDimension.class))).thenReturn(dimensionDTO);

        ScaffoldingLogPositionDimensionDTO result = service.update(dimensionDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("12.5"), result.getHeight());
        assertEquals(new BigDecimal("6.0"), result.getWidth());

        verify(dao, times(1)).findById(anyLong());
        verify(dao, times(1)).save(any(ScaffoldingLogPositionDimension.class));
        verify(dtoMapper, times(1)).updateFromDto(any(ScaffoldingLogPositionDimensionDTO.class), any(ScaffoldingLogPositionDimension.class));
    }

    @Test
    void testUpdateDimensionNotFound() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ScaffoldingLogPositionDimensionException.class, () -> service.update(dimensionDTO));
        verify(dao, times(1)).findById(anyLong());
        verify(dao, times(0)).save(any(ScaffoldingLogPositionDimension.class));
    }

    @Test
    void testUpdateDimensionFailure() {
        when(dao.findById(anyLong())).thenReturn(Optional.of(dimension));
        doNothing().when(dtoMapper).updateFromDto(any(ScaffoldingLogPositionDimensionDTO.class), any(ScaffoldingLogPositionDimension.class));
        when(dao.save(any(ScaffoldingLogPositionDimension.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> service.update(dimensionDTO));
        verify(dao, times(1)).findById(anyLong());
        verify(dao, times(1)).save(any(ScaffoldingLogPositionDimension.class));
    }

    @Test
    void testDeleteDimension() {
        Long id = 1L;
        when(dao.findById(id)).thenReturn(Optional.of(dimension));
        doNothing().when(dao).deleteById(id);

        service.delete(id);

        verify(dao, times(1)).findById(id);
        verify(dao, times(1)).deleteById(id);
    }

    @Test
    void testDeleteDimensionNotFound() {
        Long id = 1L;
        when(dao.findById(id)).thenReturn(Optional.empty());

        assertThrows(ScaffoldingLogPositionDimensionException.class, () -> service.delete(id));
        verify(dao, times(1)).findById(id);
        verify(dao, times(0)).deleteById(id);
    }

    @Test
    void testDeleteDimensionFailure() {
        Long id = 1L;
        when(dao.findById(id)).thenReturn(Optional.of(dimension));
        doThrow(new RuntimeException("Database error")).when(dao).deleteById(id);

        assertThrows(RuntimeException.class, () -> service.delete(id));
        verify(dao, times(1)).findById(id);
        verify(dao, times(1)).deleteById(id);
    }
}
