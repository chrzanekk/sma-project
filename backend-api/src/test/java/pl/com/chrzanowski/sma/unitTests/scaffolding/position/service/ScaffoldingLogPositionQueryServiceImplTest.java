package pl.com.chrzanowski.sma.unitTests.scaffolding.position.service;

import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.scaffolding.position.dao.ScaffoldingLogPositionDao;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionAuditMapper;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.position.service.ScaffoldingLogPositionQueryServiceImpl;
import pl.com.chrzanowski.sma.scaffolding.position.service.filter.ScaffoldingLogPositionFilter;
import pl.com.chrzanowski.sma.scaffolding.position.service.filter.ScaffoldingLogPositionQuerySpec;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScaffoldingLogPositionQueryServiceImplTest {

    @InjectMocks
    private ScaffoldingLogPositionQueryServiceImpl scaffoldingLogPositionQueryService;

    @Mock
    private ScaffoldingLogPositionDao scaffoldingLogPositionDao;

    @Mock
    private ScaffoldingLogPositionAuditMapper scaffoldingLogPositionAuditMapper;

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
    void findByFilter_Positive() {
        // Given
        ScaffoldingLogPositionFilter filter = ScaffoldingLogPositionFilter.builder()
                .scaffoldingNumberContains("TEST-001")
                .build();

        BooleanBuilder specification = new BooleanBuilder();
        ScaffoldingLogPosition position = new ScaffoldingLogPosition();
        ScaffoldingLogPositionAuditableDTO dto = ScaffoldingLogPositionAuditableDTO.builder().build();

        List<ScaffoldingLogPosition> positions = List.of(position);
        List<ScaffoldingLogPositionAuditableDTO> dtos = List.of(dto);

        try (MockedStatic<ScaffoldingLogPositionQuerySpec> mockedSpec = mockStatic(ScaffoldingLogPositionQuerySpec.class)) {
            mockedSpec.when(() -> ScaffoldingLogPositionQuerySpec.buildPredicate(filter))
                    .thenReturn(specification);

            when(scaffoldingLogPositionDao.findAll(specification)).thenReturn(positions);
            when(scaffoldingLogPositionAuditMapper.toDtoList(positions)).thenReturn(dtos);

            // When
            List<ScaffoldingLogPositionAuditableDTO> result = scaffoldingLogPositionQueryService.findByFilter(filter);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(dto, result.get(0));
            verify(scaffoldingLogPositionDao, times(1)).findAll(specification);
            verify(scaffoldingLogPositionAuditMapper, times(1)).toDtoList(positions);
        }
    }

    @Test
    void findByFilter_Negative() {
        // Given
        ScaffoldingLogPositionFilter filter = ScaffoldingLogPositionFilter.builder()
                .scaffoldingNumberContains("NONEXISTENT")
                .build();

        BooleanBuilder specification = new BooleanBuilder();
        List<ScaffoldingLogPosition> emptyPositions = Collections.emptyList();
        List<ScaffoldingLogPositionAuditableDTO> emptyDtos = Collections.emptyList();

        try (MockedStatic<ScaffoldingLogPositionQuerySpec> mockedSpec = mockStatic(ScaffoldingLogPositionQuerySpec.class)) {
            mockedSpec.when(() -> ScaffoldingLogPositionQuerySpec.buildPredicate(filter))
                    .thenReturn(specification);

            when(scaffoldingLogPositionDao.findAll(specification)).thenReturn(emptyPositions);
            when(scaffoldingLogPositionAuditMapper.toDtoList(emptyPositions)).thenReturn(emptyDtos);

            // When
            List<ScaffoldingLogPositionAuditableDTO> result = scaffoldingLogPositionQueryService.findByFilter(filter);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(scaffoldingLogPositionDao, times(1)).findAll(specification);
            verify(scaffoldingLogPositionAuditMapper, times(1)).toDtoList(emptyPositions);
        }
    }

    @Test
    void findByFilterWithPageable_Positive() {
        // Given
        ScaffoldingLogPositionFilter filter = ScaffoldingLogPositionFilter.builder()
                .assemblyLocationContains("Warsaw")
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        BooleanBuilder specification = new BooleanBuilder();

        ScaffoldingLogPosition position = new ScaffoldingLogPosition();
        ScaffoldingLogPositionAuditableDTO dto = ScaffoldingLogPositionAuditableDTO.builder().build();

        Page<ScaffoldingLogPosition> positionPage = new PageImpl<>(List.of(position), pageable, 1);
        Page<ScaffoldingLogPositionAuditableDTO> dtoPage = new PageImpl<>(List.of(dto), pageable, 1);

        try (MockedStatic<ScaffoldingLogPositionQuerySpec> mockedSpec = mockStatic(ScaffoldingLogPositionQuerySpec.class)) {
            mockedSpec.when(() -> ScaffoldingLogPositionQuerySpec.buildPredicate(filter))
                    .thenReturn(specification);

            when(scaffoldingLogPositionDao.findAll(specification, pageable)).thenReturn(positionPage);
            when(scaffoldingLogPositionAuditMapper.toDto(position)).thenReturn(dto);

            // When
            Page<ScaffoldingLogPositionAuditableDTO> result = scaffoldingLogPositionQueryService.findByFilter(filter, pageable);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals(1, result.getContent().size());
            assertEquals(dto, result.getContent().get(0));
            verify(scaffoldingLogPositionDao, times(1)).findAll(specification, pageable);
        }
    }

    @Test
    void findByFilterWithPageable_Negative() {
        // Given
        ScaffoldingLogPositionFilter filter = ScaffoldingLogPositionFilter.builder()
                .contractorNameContains("NONEXISTENT")
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        BooleanBuilder specification = new BooleanBuilder();

        Page<ScaffoldingLogPosition> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        try (MockedStatic<ScaffoldingLogPositionQuerySpec> mockedSpec = mockStatic(ScaffoldingLogPositionQuerySpec.class)) {
            mockedSpec.when(() -> ScaffoldingLogPositionQuerySpec.buildPredicate(filter))
                    .thenReturn(specification);

            when(scaffoldingLogPositionDao.findAll(specification, pageable)).thenReturn(emptyPage);

            // When
            Page<ScaffoldingLogPositionAuditableDTO> result = scaffoldingLogPositionQueryService.findByFilter(filter, pageable);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            assertEquals(0, result.getTotalElements());
            verify(scaffoldingLogPositionDao, times(1)).findAll(specification, pageable);
        }
    }

    @Test
    void findByFilter_WithMultipleFilters_Positive() {
        // Given
        ScaffoldingLogPositionFilter filter = ScaffoldingLogPositionFilter.builder()
                .scaffoldingNumberContains("TEST")
                .assemblyLocationContains("Warsaw")
                .contractorNameContains("ABC")
                .build();

        BooleanBuilder specification = new BooleanBuilder();
        ScaffoldingLogPosition position1 = new ScaffoldingLogPosition();
        ScaffoldingLogPosition position2 = new ScaffoldingLogPosition();

        ScaffoldingLogPositionAuditableDTO dto1 = ScaffoldingLogPositionAuditableDTO.builder().build();
        ScaffoldingLogPositionAuditableDTO dto2 = ScaffoldingLogPositionAuditableDTO.builder().build();

        List<ScaffoldingLogPosition> positions = List.of(position1, position2);
        List<ScaffoldingLogPositionAuditableDTO> dtos = List.of(dto1, dto2);

        try (MockedStatic<ScaffoldingLogPositionQuerySpec> mockedSpec = mockStatic(ScaffoldingLogPositionQuerySpec.class)) {
            mockedSpec.when(() -> ScaffoldingLogPositionQuerySpec.buildPredicate(filter))
                    .thenReturn(specification);

            when(scaffoldingLogPositionDao.findAll(specification)).thenReturn(positions);
            when(scaffoldingLogPositionAuditMapper.toDtoList(positions)).thenReturn(dtos);

            // When
            List<ScaffoldingLogPositionAuditableDTO> result = scaffoldingLogPositionQueryService.findByFilter(filter);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(scaffoldingLogPositionDao, times(1)).findAll(specification);
            verify(scaffoldingLogPositionAuditMapper, times(1)).toDtoList(positions);
        }
    }

    @Test
    void findByFilterWithPageable_WithMultiplePages_Positive() {
        // Given
        ScaffoldingLogPositionFilter filter = ScaffoldingLogPositionFilter.builder()
                .scaffoldingUserNameContains("User")
                .build();

        Pageable pageable = PageRequest.of(1, 5);
        BooleanBuilder specification = new BooleanBuilder();

        ScaffoldingLogPosition position = new ScaffoldingLogPosition();
        ScaffoldingLogPositionAuditableDTO dto = ScaffoldingLogPositionAuditableDTO.builder().build();

        Page<ScaffoldingLogPosition> positionPage = new PageImpl<>(List.of(position), pageable, 20);

        try (MockedStatic<ScaffoldingLogPositionQuerySpec> mockedSpec = mockStatic(ScaffoldingLogPositionQuerySpec.class)) {
            mockedSpec.when(() -> ScaffoldingLogPositionQuerySpec.buildPredicate(filter))
                    .thenReturn(specification);

            when(scaffoldingLogPositionDao.findAll(specification, pageable)).thenReturn(positionPage);
            when(scaffoldingLogPositionAuditMapper.toDto(position)).thenReturn(dto);

            // When
            Page<ScaffoldingLogPositionAuditableDTO> result = scaffoldingLogPositionQueryService.findByFilter(filter, pageable);

            // Then
            assertNotNull(result);
            assertEquals(20, result.getTotalElements());
            assertEquals(1, result.getContent().size());
            assertEquals(1, result.getNumber());
            assertEquals(5, result.getSize());
            verify(scaffoldingLogPositionDao, times(1)).findAll(specification, pageable);
        }
    }
}
