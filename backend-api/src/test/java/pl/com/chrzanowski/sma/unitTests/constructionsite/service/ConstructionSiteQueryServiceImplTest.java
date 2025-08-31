package pl.com.chrzanowski.sma.unitTests.constructionsite.service;

import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.com.chrzanowski.sma.constructionsite.dao.ConstructionSiteDao;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteAuditableDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteDTO;
import pl.com.chrzanowski.sma.constructionsite.mapper.ConstructionSiteAuditMapper;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.constructionsite.service.ConstructionSiteQueryServiceImpl;
import pl.com.chrzanowski.sma.constructionsite.service.filter.ConstructionSiteFilter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ConstructionSiteQueryServiceImplTest {

    @InjectMocks
    private ConstructionSiteQueryServiceImpl service;

    @Mock
    private ConstructionSiteDao dao;

    @Mock
    private ConstructionSiteAuditMapper auditMapper;

    private AutoCloseable closeable;
    private ConstructionSiteDTO baseDTO;
    private ConstructionSiteAuditableDTO dto;
    private ConstructionSite entity;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        baseDTO = ConstructionSiteDTO.builder()
                .id(1L)
                .name("TestSite")
                .build();
        dto = ConstructionSiteAuditableDTO.builder()
                .base(baseDTO).build();
        entity = new ConstructionSite();
        entity.setId(1L);
        entity.setName("TestSite");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testFindByFilterSuccess() {
        ConstructionSiteFilter filter = new ConstructionSiteFilter();
        when(dao.findAll(any(BooleanBuilder.class)))
                .thenReturn(List.of());
        when(auditMapper.toDtoList(anyList()))
                .thenReturn(List.of(dto));

        List<ConstructionSiteAuditableDTO> result = service.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(dao, times(1)).findAll(any(BooleanBuilder.class));
        verify(auditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterEmpty() {
        ConstructionSiteFilter filter = new ConstructionSiteFilter();
        when(dao.findAll(any(BooleanBuilder.class)))
                .thenReturn(Collections.emptyList());

        List<ConstructionSiteAuditableDTO> result = service.findByFilter(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dao, times(1)).findAll(any(BooleanBuilder.class));
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        ConstructionSiteFilter filter = new ConstructionSiteFilter();
        PageRequest page = PageRequest.of(0, 10);

        Page<ConstructionSite> pageOfEntities = new PageImpl<>(Collections.singletonList(entity));
        when(dao.findAll(any(BooleanBuilder.class), any(PageRequest.class)))
                .thenReturn(pageOfEntities);
        when(auditMapper.toDto(any()))
                .thenReturn(dto);

        Page<ConstructionSiteAuditableDTO> result = service.findByFilter(filter, page);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(dao, times(1)).findAll(any(BooleanBuilder.class), any(PageRequest.class));
        verify(auditMapper, times(1)).toDto(any());
    }

    @Test
    void testFindByFilterAndPageEmpty() {
        ConstructionSiteFilter filter = new ConstructionSiteFilter();
        PageRequest page = PageRequest.of(0, 10);

        when(dao.findAll(any(BooleanBuilder.class), any(PageRequest.class)))
                .thenReturn(Page.empty());

        Page<ConstructionSiteAuditableDTO> result = service.findByFilter(filter, page);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dao, times(1)).findAll(any(BooleanBuilder.class), any(PageRequest.class));
    }
}
