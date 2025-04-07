package pl.com.chrzanowski.sma.unitTests.contractor.service;

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
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.dto.ContractorAuditableDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorDTOMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.service.ContractorQueryServiceImpl;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContractorQueryServiceImplTest {

    @Mock
    private ContractorDao contractorDao;

    @Mock
    private ContractorDTOMapper contractorMapper;

    @InjectMocks
    private ContractorQueryServiceImpl contractorQueryService;

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
    void testFindByFilterSuccess() {
        ContractorFilter filter = new ContractorFilter();

        when(contractorDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(contractor));
        when(contractorMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(contractorDTO));

        List<ContractorAuditableDTO> result = contractorQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(contractorDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(contractorMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        ContractorFilter filter = new ContractorFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Contractor> workshopPage = new PageImpl<>(Collections.singletonList(contractor));
        when(contractorDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(workshopPage);
        when(contractorMapper.toDto(any(Contractor.class))).thenReturn(contractorDTO);

        Page<ContractorDTO> result = contractorQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(contractorDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(contractorMapper, times(1)).toDto(any(Contractor.class));
    }
}
