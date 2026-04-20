package pl.com.chrzanowski.sma.unitTests.unit.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.exception.UnitException;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.unit.dao.UnitDao;
import pl.com.chrzanowski.sma.unit.dto.UnitBaseDTO;
import pl.com.chrzanowski.sma.unit.dto.UnitDTO;
import pl.com.chrzanowski.sma.unit.mapper.UnitDTOMapper;
import pl.com.chrzanowski.sma.unit.model.Unit;
import pl.com.chrzanowski.sma.unit.service.UnitServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UnitServiceImplTest {

    @Mock
    private UnitDao unitDao;

    @Mock
    private UnitDTOMapper unitDTOMapper;

    @InjectMocks
    private UnitServiceImpl unitService;

    private UnitDTO unitDTO;
    private UnitDTO nonGlobalUnitDTO;
    private Unit unit;
    private Unit nonGlobalUnit;
    private AutoCloseable autoCloseable;
    private Company company;
    private CompanyDTO companyDTO;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        unitDTO = UnitDTO.builder()
                .id(1L)
                .symbol("m2")
                .build();

        unit = new Unit();
        unit.setId(1L);
        unit.setSymbol("m2");

        company = new Company();
        company.setId(1L);
        company.setName("TestCompany");
        company.setAdditionalInfo("AdditionalInfo");

        companyDTO = CompanyDTO.builder()
                .id(1L)
                .name("TestCompany")
                .additionalInfo("AdditionalInfo")
                .build();

        nonGlobalUnitDTO = UnitDTO.builder()
                .id(2L)
                .symbol("mHA")
                .company(companyDTO)
                .build();

        nonGlobalUnit = new Unit();
        nonGlobalUnit.setId(2L);
        nonGlobalUnit.setSymbol("mHA");
        nonGlobalUnit.setCompany(company);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveUnit() {
        when(unitDTOMapper.toEntity(any(UnitDTO.class))).thenReturn(unit);
        when(unitDao.save(any(Unit.class))).thenReturn(unit);
        when(unitDTOMapper.toDto(any(Unit.class))).thenReturn(unitDTO);

        UnitDTO result = unitService.save(unitDTO);
        assertNotNull(result);
        assertEquals("m2", result.getSymbol());
        assertEquals(1L, result.getId());

        verify(unitDao, times(1)).save(any(Unit.class));
        verify(unitDTOMapper, times(1)).toDto(any(Unit.class));
        verify(unitDTOMapper, times(1)).toEntity(any(UnitDTO.class));
    }

    @Test
    void testSaveUnitFailure() {
        when(unitDTOMapper.toEntity(any(UnitDTO.class))).thenReturn(unit);
        when(unitDao.save(any(Unit.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> unitService.save(unitDTO));
        verify(unitDao, times(1)).save(any(Unit.class));
    }

    @Test
    void testFindById() {
        when(unitDao.findById(1L)).thenReturn(Optional.of(unit));
        when(unitDTOMapper.toDto(any(Unit.class))).thenReturn(unitDTO);

        UnitDTO result = unitService.findById(1L);
        assertNotNull(result);
        assertEquals("m2", result.getSymbol());
        assertEquals(1L, result.getId());

        verify(unitDao, times(1)).findById(1L);
        verify(unitDTOMapper, times(1)).toDto(any(Unit.class));
    }

    @Test
    void testFindByIdNotFound() {
        when(unitDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UnitException.class, () -> unitService.findById(1L));
        verify(unitDao, times(1)).findById(1L);
        verify(unitDTOMapper, never()).toDto(any(Unit.class));
    }

    @Test
    void testUpdateUnit() {
        when(unitDao.findById(anyLong())).thenReturn(Optional.of(nonGlobalUnit));
        doNothing().when(unitDTOMapper).updateFromDto(any(UnitDTO.class), any(Unit.class));
        when(unitDao.save(any(Unit.class))).thenReturn(nonGlobalUnit);
        when(unitDTOMapper.toDto(any(Unit.class))).thenReturn(nonGlobalUnitDTO);

        UnitBaseDTO result = unitService.update(nonGlobalUnitDTO);
        assertNotNull(result);
        assertEquals("mHA", result.getSymbol());
        assertEquals(2L, result.getId());

        verify(unitDao, times(1)).findById(anyLong());
        verify(unitDTOMapper, times(1)).updateFromDto(any(UnitDTO.class), any(Unit.class));
        verify(unitDao, times(1)).save(any(Unit.class));
        verify(unitDTOMapper, times(1)).toDto(any(Unit.class));
    }

    @Test
    void testUpdateUnitNotFound() {
        when(unitDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UnitException.class, () -> unitService.update(unitDTO));
        verify(unitDao, times(1)).findById(anyLong());
        verify(unitDTOMapper, never()).updateFromDto(any(UnitDTO.class), any(Unit.class));
        verify(unitDao, never()).save(any(Unit.class));
    }

    @Test
    void testDeleteUnit() {
        Long id = 1L;
        when(unitDao.findById(id)).thenReturn(Optional.of(nonGlobalUnit));
        doNothing().when(unitDao).deleteById(id);

        unitService.delete(id);

        verify(unitDao, times(1)).findById(id);
        verify(unitDao, times(1)).deleteById(id);
    }

    @Test
    void testDeleteUnitFailure() {
        Long id = 1L;
        when(unitDao.findById(id)).thenReturn(Optional.of(nonGlobalUnit));
        doThrow(new RuntimeException("Delete failed")).when(unitDao).deleteById(id);

        assertThrows(RuntimeException.class, () -> unitService.delete(id));

        verify(unitDao, times(1)).findById(id);
        verify(unitDao, times(1)).deleteById(id);
    }
}
