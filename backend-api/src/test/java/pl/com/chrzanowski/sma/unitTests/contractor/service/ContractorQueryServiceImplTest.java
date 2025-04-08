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
import pl.com.chrzanowski.sma.contractor.mapper.ContractorAuditMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.service.ContractorQueryServiceImpl;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;

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
    private ContractorAuditMapper contractorAuditMapper;

    @InjectMocks
    private ContractorQueryServiceImpl contractorQueryService;

    private ContractorAuditableDTO contractorAuditableDTO;
    private ContractorDTO contractorDTO;
    private Contractor contractor;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        contractorDTO = ContractorDTO.builder().id(1L)
                .name("Workshop 1")
                .build();

        contractorAuditableDTO = ContractorAuditableDTO.builder()
                .base(contractorDTO)
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
        when(contractorAuditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(contractorAuditableDTO));

        List<ContractorAuditableDTO> result = contractorQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(contractorDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(contractorAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        ContractorFilter filter = new ContractorFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Contractor> workshopPage = new PageImpl<>(Collections.singletonList(contractor));
        when(contractorDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(workshopPage);
        when(contractorAuditMapper.toDto(any(Contractor.class))).thenReturn(contractorAuditableDTO);

        Page<ContractorAuditableDTO> result = contractorQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(contractorDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(contractorAuditMapper, times(1)).toDto(any(Contractor.class));
    }
}
