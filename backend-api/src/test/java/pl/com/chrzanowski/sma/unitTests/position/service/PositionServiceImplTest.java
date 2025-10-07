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
import pl.com.chrzanowski.sma.position.mapper.PositionBaseMapper;
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
    private PositionBaseMapper positionBaseMapper;

    @InjectMocks
    private PositionServiceImpl positionService;

    private PositionBaseDTO positionBaseDTO;
    private Position position;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        positionBaseDTO = PositionBaseDTO.builder()
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
        when(positionBaseMapper.toEntity(any(PositionBaseDTO.class))).thenReturn(position);
        when(positionDao.save(any(Position.class))).thenReturn(position);
        when(positionBaseMapper.toDto(any(Position.class))).thenReturn(positionBaseDTO);

        PositionBaseDTO result = positionService.save(positionBaseDTO);
        assertNotNull(result);
        assertEquals("Manager", result.getName());
        assertEquals(1L, result.getId());

        verify(positionDao, times(1)).save(any(Position.class));
        verify(positionBaseMapper, times(1)).toDto(any(Position.class));
        verify(positionBaseMapper, times(1)).toEntity(any(PositionBaseDTO.class));
    }

    @Test
    void testSavePositionFailure() {
        when(positionBaseMapper.toEntity(any(PositionBaseDTO.class))).thenReturn(position);
        when(positionDao.save(any(Position.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> positionService.save(positionBaseDTO));
        verify(positionDao, times(1)).save(any(Position.class));
    }

    @Test
    void testFindById() {
        when(positionDao.findById(1L)).thenReturn(Optional.of(position));
        when(positionBaseMapper.toDto(any(Position.class))).thenReturn(positionBaseDTO);

        PositionBaseDTO result = positionService.findById(1L);
        assertNotNull(result);
        assertEquals("Manager", result.getName());
        assertEquals(1L, result.getId());

        verify(positionDao, times(1)).findById(1L);
        verify(positionBaseMapper, times(1)).toDto(any(Position.class));
    }

    @Test
    void testFindByIdNotFound() {
        when(positionDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PositionException.class, () -> positionService.findById(1L));
        verify(positionDao, times(1)).findById(1L);
        verify(positionBaseMapper, never()).toDto(any(Position.class));
    }

    @Test
    void testUpdatePosition() {
        when(positionDao.findById(anyLong())).thenReturn(Optional.of(position));
        doNothing().when(positionBaseMapper).updateFromBaseDto(any(PositionBaseDTO.class), any(Position.class));
        when(positionDao.save(any(Position.class))).thenReturn(position);
        when(positionBaseMapper.toDto(any(Position.class))).thenReturn(positionBaseDTO);

        PositionBaseDTO result = positionService.update(positionBaseDTO);
        assertNotNull(result);
        assertEquals("Manager", result.getName());
        assertEquals(1L, result.getId());

        verify(positionDao, times(1)).findById(anyLong());
        verify(positionBaseMapper, times(1)).updateFromBaseDto(any(PositionBaseDTO.class), any(Position.class));
        verify(positionDao, times(1)).save(any(Position.class));
        verify(positionBaseMapper, times(1)).toDto(any(Position.class));
    }

    @Test
    void testUpdatePositionNotFound() {
        when(positionDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PositionException.class, () -> positionService.update(positionBaseDTO));
        verify(positionDao, times(1)).findById(anyLong());
        verify(positionBaseMapper, never()).updateFromBaseDto(any(PositionBaseDTO.class), any(Position.class));
        verify(positionDao, never()).save(any(Position.class));
    }

    @Test
    void testDeletePosition() {
        Long id = 1L;
        doNothing().when(positionDao).deleteById(id);

        positionService.delete(id);

        verify(positionDao, times(1)).deleteById(id);
    }

    @Test
    void testDeletePositionFailure() {
        Long id = 1L;
        doThrow(new RuntimeException("Delete failed")).when(positionDao).deleteById(id);

        assertThrows(RuntimeException.class, () -> positionService.delete(id));
        verify(positionDao, times(1)).deleteById(id);
    }
}
