package pl.com.chrzanowski.sma.unitTests.contractor.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.service.ContractorServiceImpl;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorQuerySpec;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContractorServiceImplTest {

    @Mock
    private ContractorDao contractorDao;

    @Mock
    private ContractorMapper contractorMapper;

    @Mock
    private ContractorQuerySpec contractorQuerySpec;

    @InjectMocks
    private ContractorServiceImpl contractorService;

    private ContractorDTO contractorDTO;
    private Contractor contractor;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        contractorDTO = ContractorDTO.builder()
                .id(1L)
                .name("Workshop 1")
                .createdDatetime(Instant.now())
                .build();

        contractor = new Contractor();
        contractor.setId(1L);
        contractor.setName("Workshop 1");
    }


    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveContractorSuccess() {
        when(contractorMapper.toEntity(any(ContractorDTO.class))).thenReturn(contractor);
        when(contractorDao.save(any(Contractor.class))).thenReturn(contractor);
        when(contractorMapper.toDto(any(Contractor.class))).thenReturn(contractorDTO);

        ContractorDTO result = contractorService.save(contractorDTO);

        assertNotNull(result);
        assertEquals("Workshop 1", result.getName());

        verify(contractorMapper, times(1)).toEntity(any(ContractorDTO.class));
        verify(contractorDao, times(1)).save(any(Contractor.class));
        verify(contractorMapper, times(1)).toDto(any(Contractor.class));
    }

    @Test
    void testUpdateContractorSuccess() {
        when(contractorMapper.toEntity(any(ContractorDTO.class))).thenReturn(contractor);
        when(contractorDao.save(any(Contractor.class))).thenReturn(contractor);
        when(contractorMapper.toDto(any(Contractor.class))).thenReturn(contractorDTO);

        ContractorDTO result = contractorService.update(contractorDTO);

        assertNotNull(result);
        assertEquals("Workshop 1", result.getName());

        verify(contractorMapper, times(1)).toEntity(any(ContractorDTO.class));
        verify(contractorDao, times(1)).save(any(Contractor.class));
        verify(contractorMapper, times(1)).toDto(any(Contractor.class));
    }

    @Test
    void testFindByIdSuccess() {
        when(contractorDao.findById(1L)).thenReturn(Optional.of(contractor));
        when(contractorMapper.toDto(any(Contractor.class))).thenReturn(contractorDTO);

        ContractorDTO result = contractorService.findById(1L);

        assertNotNull(result);
        assertEquals("Workshop 1", result.getName());

        verify(contractorDao, times(1)).findById(1L);
        verify(contractorMapper, times(1)).toDto(any(Contractor.class));
    }

    @Test
    void testFindByIdNotFound() {
        when(contractorDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> contractorService.findById(1L));

        verify(contractorDao, times(1)).findById(1L);
    }

    @Test
    void testFindAllSuccess() {
        when(contractorDao.findAll()).thenReturn(Collections.singletonList(contractor));
        when(contractorMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(contractorDTO));

        List<ContractorDTO> result = contractorService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(contractorDao, times(1)).findAll();
        verify(contractorMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testDeleteSuccess() {
        contractorService.delete(1L);

        verify(contractorDao, times(1)).deleteById(1L);
    }
}
