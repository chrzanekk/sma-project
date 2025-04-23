package pl.com.chrzanowski.sma.unitTests.constructionsite.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.exception.ConstructionSiteException;
import pl.com.chrzanowski.sma.constructionsite.dao.ConstructionSiteDao;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteDTO;
import pl.com.chrzanowski.sma.constructionsite.mapper.ConstructionSiteBaseMapper;
import pl.com.chrzanowski.sma.constructionsite.mapper.ConstructionSiteDTOMapper;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.constructionsite.service.ConstructionSiteServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConstructionSiteServiceImplTest {

    @InjectMocks
    private ConstructionSiteServiceImpl service;

    @Mock
    private ConstructionSiteDao dao;

    @Mock
    private ConstructionSiteDTOMapper dtoMapper;

    private AutoCloseable closeable;
    private ConstructionSiteDTO dto;
    private ConstructionSite entity;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        dto = ConstructionSiteDTO.builder()
                .id(1L)
                .name("TestSite")
                .build();
        entity = new ConstructionSite();
        entity.setId(1L);
        entity.setName("TestSite");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSave() {
        when(dtoMapper.toEntity(any(ConstructionSiteDTO.class))).thenReturn(entity);
        when(dao.save(any(ConstructionSite.class))).thenReturn(entity);
        when(dtoMapper.toDto(any(ConstructionSite.class))).thenReturn(dto);

        ConstructionSiteDTO result = service.save(dto);

        assertNotNull(result);
        assertEquals("TestSite", result.getName());
        verify(dao, times(1)).save(entity);
        verify(dtoMapper, times(1)).toDto(entity);
    }

    @Test
    void testSaveFailure() {
        when(dtoMapper.toEntity(any(ConstructionSiteDTO.class))).thenReturn(entity);
        when(dao.save(any(ConstructionSite.class))).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> service.save(dto));
        verify(dao, times(1)).save(any());
    }

    @Test
    void testUpdate_Positive() {
        when(dao.findById(1L)).thenReturn(Optional.of(entity));
        doNothing().when(dtoMapper).updateFromDto(eq(dto), eq(entity));
        when(dao.save(entity)).thenReturn(entity);
        when(dtoMapper.toDto(entity)).thenReturn(dto);

        ConstructionSiteDTO result = service.update(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(dao, times(1)).findById(1L);
        verify(dao, times(1)).save(entity);
    }

    @Test
    void testUpdate_NotFound() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ConstructionSiteException.class, () -> service.update(dto));
        verify(dao, times(1)).findById(anyLong());
    }

    @Test
    void testFindById_Positive() {
        when(dao.findById(1L)).thenReturn(Optional.of(entity));
        when(dtoMapper.toDto(entity)).thenReturn(dto);

        ConstructionSiteDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals("TestSite", result.getName());
        verify(dao, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ConstructionSiteException.class, () -> service.findById(99L));
        verify(dao, times(1)).findById(99L);
    }

    @Test
    void testDelete_Positive() {
        doNothing().when(dao).deleteById(1L);

        service.delete(1L);

        verify(dao, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_Failure() {
        doThrow(new RuntimeException("fail")).when(dao).deleteById(2L);

        assertThrows(RuntimeException.class, () -> service.delete(2L));
        verify(dao, times(1)).deleteById(2L);
    }
}
