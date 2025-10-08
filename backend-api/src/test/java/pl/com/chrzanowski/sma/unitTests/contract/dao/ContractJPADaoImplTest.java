
package pl.com.chrzanowski.sma.unitTests.contract.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.contract.dao.ContractJPADaoImpl;
import pl.com.chrzanowski.sma.contract.model.Contract;
import pl.com.chrzanowski.sma.contract.model.QContract;
import pl.com.chrzanowski.sma.contract.repository.ContractRepository;
import pl.com.chrzanowski.sma.contract.service.filter.ContractQuerySpec;
import pl.com.chrzanowski.sma.helper.SimplePagedList;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ContractJPADaoImplTest {

    @InjectMocks
    private ContractJPADaoImpl contractJPADaoImpl;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ContractQuerySpec contractQuerySpec;

    @Mock
    private BlazeJPAQueryFactory queryFactory;

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
    void save_Positive() {
        Contract contract = new Contract();
        when(contractRepository.save(contract)).thenReturn(contract);
        assertEquals(contract, contractJPADaoImpl.save(contract));
        verify(contractRepository).save(contract);
    }

    @Test
    void saveAll_Positive() {
        Contract contract = new Contract();
        List<Contract> contracts = List.of(contract);
        when(contractRepository.saveAll(contracts)).thenReturn(contracts);
        assertEquals(contracts, contractJPADaoImpl.saveAll(contracts));
        verify(contractRepository).saveAll(contracts);
    }

    @Test
    void findById_Positive() {
        Long id = 1L;
        Contract contract = new Contract();
        when(contractRepository.findById(id)).thenReturn(Optional.of(contract));
        assertEquals(Optional.of(contract), contractJPADaoImpl.findById(id));
        verify(contractRepository).findById(id);
    }

    @Test
    void findById_Negative() {
        Long id = 999L;
        when(contractRepository.findById(id)).thenReturn(Optional.empty());
        assertTrue(contractJPADaoImpl.findById(id).isEmpty());
        verify(contractRepository).findById(id);
    }

    @Test
    void findAll_Positive() {
        Contract contract = new Contract();
        List<Contract> contracts = List.of(contract);
        when(contractRepository.findAll()).thenReturn(contracts);
        assertEquals(contracts, contractJPADaoImpl.findAll());
        verify(contractRepository).findAll();
    }

    @Test
    void findAllWithSpecification_Positive() {
        BooleanBuilder spec = new BooleanBuilder();
        Contract contract = new Contract();
        List<Contract> contracts = List.of(contract);

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Contract> query = mock(BlazeJPAQuery.class);
        when(contractQuerySpec.buildQuery(spec, null)).thenReturn(query);
        when(query.fetch()).thenReturn(contracts);

        List<Contract> result = contractJPADaoImpl.findAll(spec);
        assertEquals(1, result.size());
        verify(contractQuerySpec).buildQuery(spec, null);
    }

    @Test
    void findAllWithSpecificationAndPageable_Positive() {
        // Given
        BooleanBuilder spec = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);
        Contract contract = new Contract();

        // Mock query z RETURNS_DEEP_STUBS aby obsłużyć fluent API
        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Contract> query = mock(BlazeJPAQuery.class, RETURNS_DEEP_STUBS);

        when(contractQuerySpec.buildQuery(spec, pageable)).thenReturn(query);

        // Mock fetchPage - to jest ostateczne wywołanie w łańcuchu
        PagedList<Contract> pagedList = new SimplePagedList<>(List.of(contract), 1);
        when(query.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<Contract> result = contractJPADaoImpl.findAll(spec, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(contract, result.getContent().get(0));

        verify(contractQuerySpec).buildQuery(spec, pageable);
        verify(query).fetchPage(0, 10);
    }

    @Test
    void deleteById_Positive() {
        Long id = 1L;
        contractJPADaoImpl.deleteById(id);
        verify(contractRepository).deleteById(id);
    }

    @Test
    void findByContractorId_Positive() {
        Long contractorId = 5L;
        Pageable pageable = PageRequest.of(0, 10);
        Contract contract = new Contract();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Contract> query = mock(BlazeJPAQuery.class);
        when(queryFactory.selectFrom(QContract.contract)).thenReturn(query);
        when(query.where(QContract.contract.contractor.id.eq(contractorId))).thenReturn(query);
        when(query.orderBy(QContract.contract.id.asc())).thenReturn(query);

        PagedList<Contract> pagedList = new SimplePagedList<>(List.of(contract), 1);
        when(query.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        Page<Contract> result = contractJPADaoImpl.findByContractorId(contractorId, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(contract, result.getContent().get(0));
    }
}
