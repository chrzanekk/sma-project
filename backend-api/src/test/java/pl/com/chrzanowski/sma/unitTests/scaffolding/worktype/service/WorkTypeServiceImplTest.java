package pl.com.chrzanowski.sma.unitTests.scaffolding.worktype.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.exception.WorkTypeException;
import pl.com.chrzanowski.sma.scaffolding.worktype.dao.WorkTypeDao;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.mapper.WorkTypeDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.worktype.model.WorkType;
import pl.com.chrzanowski.sma.scaffolding.worktype.service.WorkTypeServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkTypeServiceImplTest {

    @Mock
    private WorkTypeDao workTypeDao;

    @Mock
    private WorkTypeDTOMapper workTypeDTOMapper;

    @InjectMocks
    private WorkTypeServiceImpl workTypeService;

    private WorkTypeDTO workTypeDTO;
    private WorkType workType;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        workTypeDTO = WorkTypeDTO.builder()
                .id(1L)
                .name("Scaffolding Assembly")
                .build();

        workType = new WorkType();
        workType.setId(1L);
        workType.setName("Scaffolding Assembly");
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveWorkType() {
        when(workTypeDTOMapper.toEntity(any(WorkTypeDTO.class))).thenReturn(workType);
        when(workTypeDao.save(any(WorkType.class))).thenReturn(workType);
        when(workTypeDTOMapper.toDto(any(WorkType.class))).thenReturn(workTypeDTO);

        WorkTypeDTO result = workTypeService.save(workTypeDTO);
        assertNotNull(result);
        assertEquals("Scaffolding Assembly", result.getName());
        assertEquals(1L, result.getId());

        verify(workTypeDao, times(1)).save(any(WorkType.class));
        verify(workTypeDTOMapper, times(1)).toDto(any(WorkType.class));
        verify(workTypeDTOMapper, times(1)).toEntity(any(WorkTypeDTO.class));
    }

    @Test
    void testSaveWorkTypeFailure() {
        when(workTypeDTOMapper.toEntity(any(WorkTypeDTO.class))).thenReturn(workType);
        when(workTypeDao.save(any(WorkType.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> workTypeService.save(workTypeDTO));
        verify(workTypeDao, times(1)).save(any(WorkType.class));
    }

    @Test
    void testFindById() {
        when(workTypeDao.findById(1L)).thenReturn(Optional.of(workType));
        when(workTypeDTOMapper.toDto(any(WorkType.class))).thenReturn(workTypeDTO);

        WorkTypeDTO result = workTypeService.findById(1L);
        assertNotNull(result);
        assertEquals("Scaffolding Assembly", result.getName());
        assertEquals(1L, result.getId());

        verify(workTypeDao, times(1)).findById(1L);
        verify(workTypeDTOMapper, times(1)).toDto(any(WorkType.class));
    }

    @Test
    void testFindByIdNotFound() {
        when(workTypeDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkTypeException.class, () -> workTypeService.findById(1L));
        verify(workTypeDao, times(1)).findById(1L);
        verify(workTypeDTOMapper, never()).toDto(any(WorkType.class));
    }

    @Test
    void testUpdateWorkType() {
        when(workTypeDao.findById(anyLong())).thenReturn(Optional.of(workType));
        doNothing().when(workTypeDTOMapper).updateFromDto(any(WorkTypeDTO.class), any(WorkType.class));
        when(workTypeDao.save(any(WorkType.class))).thenReturn(workType);
        when(workTypeDTOMapper.toDto(any(WorkType.class))).thenReturn(workTypeDTO);

        WorkTypeDTO result = workTypeService.update(workTypeDTO);
        assertNotNull(result);
        assertEquals("Scaffolding Assembly", result.getName());
        assertEquals(1L, result.getId());

        verify(workTypeDao, times(1)).findById(anyLong());
        verify(workTypeDTOMapper, times(1)).updateFromDto(any(WorkTypeDTO.class), any(WorkType.class));
        verify(workTypeDao, times(1)).save(any(WorkType.class));
        verify(workTypeDTOMapper, times(1)).toDto(any(WorkType.class));
    }

    @Test
    void testUpdateWorkTypeNotFound() {
        when(workTypeDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(WorkTypeException.class, () -> workTypeService.update(workTypeDTO));
        verify(workTypeDao, times(1)).findById(anyLong());
        verify(workTypeDTOMapper, never()).updateFromDto(any(WorkTypeDTO.class), any(WorkType.class));
        verify(workTypeDao, never()).save(any(WorkType.class));
    }

    @Test
    void testDeleteWorkType() {
        Long id = 1L;
        when(workTypeDao.findById(id)).thenReturn(Optional.of(workType));
        doNothing().when(workTypeDao).deleteById(id);

        workTypeService.delete(id);

        verify(workTypeDao, times(1)).findById(id);
        verify(workTypeDao, times(1)).deleteById(id);
    }

    @Test
    void testDeleteWorkTypeNotFound() {
        Long id = 1L;
        when(workTypeDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(WorkTypeException.class, () -> workTypeService.delete(id));
        verify(workTypeDao, times(1)).findById(id);
        verify(workTypeDao, never()).deleteById(id);
    }

    @Test
    void testDeleteWorkTypeFailure() {
        Long id = 1L;
        when(workTypeDao.findById(id)).thenReturn(Optional.of(workType));
        doThrow(new RuntimeException("Delete failed")).when(workTypeDao).deleteById(id);

        assertThrows(RuntimeException.class, () -> workTypeService.delete(id));

        verify(workTypeDao, times(1)).findById(id);
        verify(workTypeDao, times(1)).deleteById(id);
    }
}
