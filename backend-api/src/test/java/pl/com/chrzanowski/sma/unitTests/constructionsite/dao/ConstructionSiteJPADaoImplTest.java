package pl.com.chrzanowski.sma.unitTests.constructionsite.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.com.chrzanowski.sma.constructionsite.dao.ConstructionSiteJPADaoImpl;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.constructionsite.repository.ConstructionSiteRepository;
import pl.com.chrzanowski.sma.constructionsite.service.filter.ConstructionSiteQuerySpec;
import pl.com.chrzanowski.sma.helper.SimplePagedList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ConstructionSiteJPADaoImplTest {

    @InjectMocks
    private ConstructionSiteJPADaoImpl dao;

    @Mock
    private ConstructionSiteRepository repository;

    @Mock
    private ConstructionSiteQuerySpec querySpec;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSave() {
        ConstructionSite site = new ConstructionSite();
        when(repository.save(any(ConstructionSite.class))).thenReturn(site);

        ConstructionSite result = dao.save(site);

        assertSame(site, result);
        verify(repository, times(1)).save(site);
    }

    @Test
    void testFindById_Positive() {
        ConstructionSite site = new ConstructionSite();
        when(repository.findById(1L)).thenReturn(Optional.of(site));

        Optional<ConstructionSite> opt = dao.findById(1L);

        assertTrue(opt.isPresent());
        assertSame(site, opt.get());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testFindById_Negative() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        Optional<ConstructionSite> opt = dao.findById(2L);

        assertTrue(opt.isEmpty());
        verify(repository, times(1)).findById(2L);
    }

    @Test
    void findAll_Positive() {
        // arrange
        BooleanBuilder spec = new BooleanBuilder();
        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ConstructionSite> mockQuery = mock(BlazeJPAQuery.class);
        when(querySpec.buildQuery(spec, null)).thenReturn(mockQuery);

        ConstructionSite site = new ConstructionSite();
        List<ConstructionSite> expected = List.of(site);
        when(mockQuery.fetch()).thenReturn(expected);

        // act
        List<ConstructionSite> result = dao.findAll(spec);

        // assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(site, result.getFirst());
        verify(querySpec, times(1)).buildQuery(spec, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAll_Negative() {
        // arrange
        BooleanBuilder spec = new BooleanBuilder();
        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ConstructionSite> mockQuery = mock(BlazeJPAQuery.class);
        when(querySpec.buildQuery(spec, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.emptyList());

        // act
        List<ConstructionSite> result = dao.findAll(spec);

        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(querySpec, times(1)).buildQuery(spec, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void testFindAll_SpecAndPage_Positive() {
        BooleanBuilder spec = new BooleanBuilder();
        PageRequest page = PageRequest.of(0, 5);
        ConstructionSite constructionSite = new ConstructionSite();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ConstructionSite> mockQuery = mock(BlazeJPAQuery.class);
        when(querySpec.buildQuery(spec, page)).thenReturn(mockQuery);
        when(mockQuery.leftJoin(any(EntityPath.class))).thenReturn(mockQuery);
        when(mockQuery.fetchJoin()).thenReturn(mockQuery);

        PagedList<ConstructionSite> paged = new SimplePagedList<>(List.of(constructionSite), 1);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(paged);

        Page<ConstructionSite> result = dao.findAll(spec, page);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        verify(querySpec, times(1)).buildQuery(spec, page);
        verify(mockQuery, times(1)).fetchPage((int) page.getOffset(), page.getPageSize());
    }

    @Test
    void testFindAll_SpecAndPage_Empty() {
        BooleanBuilder spec = new BooleanBuilder();
        PageRequest page = PageRequest.of(0, 5);

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ConstructionSite> mockQuery = mock(BlazeJPAQuery.class);
        when(querySpec.buildQuery(spec, page)).thenReturn(mockQuery);
        when(mockQuery.leftJoin(any(EntityPath.class))).thenReturn(mockQuery);
        when(mockQuery.fetchJoin()).thenReturn(mockQuery);

        PagedList<ConstructionSite> paged = new SimplePagedList<>(Collections.emptyList(), 0);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(paged);

        Page<ConstructionSite> result = dao.findAll(spec, page);

        assertTrue(result.isEmpty());
        verify(querySpec, times(1)).buildQuery(spec, page);
    }

    @Test
    void testDeleteById_Positive() {
        dao.deleteById(5L);
        verify(repository, times(1)).deleteById(5L);
    }

    @Test
    void testDeleteById_Negative() {
        doThrow(new RuntimeException("fail")).when(repository).deleteById(7L);
        assertThrows(RuntimeException.class, () -> dao.deleteById(7L));
        verify(repository, times(1)).deleteById(7L);
    }
}
