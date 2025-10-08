package pl.com.chrzanowski.sma.unitTests.position.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.exception.PositionException;
import pl.com.chrzanowski.sma.position.dao.PositionDao;
import pl.com.chrzanowski.sma.position.dto.PositionBaseDTO;
import pl.com.chrzanowski.sma.position.dto.PositionDTO;
import pl.com.chrzanowski.sma.position.mapper.PositionDTOMapper;
import pl.com.chrzanowski.sma.position.model.Position;
import pl.com.chrzanowski.sma.position.service.PositionServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PositionServiceImplTest {

    @Mock
    private PositionDao positionDao;

    @Mock
    private PositionDTOMapper positionDTOMapper;

    @InjectMocks
    private PositionServiceImpl positionService;

    private PositionDTO positionDTO;
    private Position position;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        positionDTO = PositionDTO.builder()
                .id(1L)
                .name("Manager")
                .build();

        position = new Position();
        position.setId(1L);
        position.setName("Manager");
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSavePosition() {
        when(positionDTOMapper.toEntity(any(PositionDTO.class))).thenReturn(position);
        when(positionDao.save(any(Position.class))).thenReturn(position);
        when(positionDTOMapper.toDto(any(Position.class))).thenReturn(positionDTO);

        PositionDTO result = positionService.save(positionDTO);
        assertNotNull(result);
        assertEquals("Manager", result.getName());
        assertEquals(1L, result.getId());

        verify(positionDao, times(1)).save(any(Position.class));
        verify(positionDTOMapper, times(1)).toDto(any(Position.class));
        verify(positionDTOMapper, times(1)).toEntity(any(PositionDTO.class));
    }

    @Test
    void testSavePositionFailure() {
        when(positionDTOMapper.toEntity(any(PositionDTO.class))).thenReturn(position);
        when(positionDao.save(any(Position.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> positionService.save(positionDTO));
        verify(positionDao, times(1)).save(any(Position.class));
    }

    @Test
    void testFindById() {
        when(positionDao.findById(1L)).thenReturn(Optional.of(position));
        when(positionDTOMapper.toDto(any(Position.class))).thenReturn(positionDTO);

        PositionDTO result = positionService.findById(1L);
        assertNotNull(result);
        assertEquals("Manager", result.getName());
        assertEquals(1L, result.getId());

        verify(positionDao, times(1)).findById(1L);
        verify(positionDTOMapper, times(1)).toDto(any(Position.class));
    }

    @Test
    void testFindByIdNotFound() {
        when(positionDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PositionException.class, () -> positionService.findById(1L));
        verify(positionDao, times(1)).findById(1L);
        verify(positionDTOMapper, never()).toDto(any(Position.class));
    }

    @Test
    void testUpdatePosition() {
        when(positionDao.findById(anyLong())).thenReturn(Optional.of(position));
        doNothing().when(positionDTOMapper).updateFromDto(any(PositionDTO.class), any(Position.class));
        when(positionDao.save(any(Position.class))).thenReturn(position);
        when(positionDTOMapper.toDto(any(Position.class))).thenReturn(positionDTO);

        PositionBaseDTO result = positionService.update(positionDTO);
        assertNotNull(result);
        assertEquals("Manager", result.getName());
        assertEquals(1L, result.getId());

        verify(positionDao, times(1)).findById(anyLong());
        verify(positionDTOMapper, times(1)).updateFromDto(any(PositionDTO.class), any(Position.class));
        verify(positionDao, times(1)).save(any(Position.class));
        verify(positionDTOMapper, times(1)).toDto(any(Position.class));
    }

    @Test
    void testUpdatePositionNotFound() {
        when(positionDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PositionException.class, () -> positionService.update(positionDTO));
        verify(positionDao, times(1)).findById(anyLong());
        verify(positionDTOMapper, never()).updateFromDto(any(PositionDTO.class), any(Position.class));
        verify(positionDao, never()).save(any(Position.class));
    }

    @Test
    void testDeletePosition() {
        Long id = 1L;
        when(positionDao.findById(id)).thenReturn(Optional.of(position));
        doNothing().when(positionDao).deleteById(id);

        positionService.delete(id);

        verify(positionDao, times(1)).findById(id);
        verify(positionDao, times(1)).deleteById(id);
    }

    @Test
    void testDeletePositionFailure() {
        Long id = 1L;
        when(positionDao.findById(id)).thenReturn(Optional.of(position));
        doThrow(new RuntimeException("Delete failed")).when(positionDao).deleteById(id);

        assertThrows(RuntimeException.class, () -> positionService.delete(id));

        verify(positionDao, times(1)).findById(id);
        verify(positionDao, times(1)).deleteById(id);
    }
}
