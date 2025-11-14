package pl.com.chrzanowski.sma.scaffolding.position.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.mapper.ScaffoldingLogPositionDimensionDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.service.ScaffoldingLogPositionDimensionService;
import pl.com.chrzanowski.sma.scaffolding.log.mapper.ScaffoldingLogDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.log.service.ScaffoldingLogService;
import pl.com.chrzanowski.sma.scaffolding.position.dao.ScaffoldingLogPositionDao;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionDTO;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.mapper.ScaffoldingLogPositionWorkingTimeDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.service.ScaffoldingLogPositionWorkingTimeService;

import java.math.BigDecimal;
import java.util.List;


@Service
@Transactional
public class ScaffoldingLogPositionServiceImpl implements ScaffoldingLogPositionService {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionServiceImpl.class);

    private final ScaffoldingLogPositionDao scaffoldingLogPositionDao;
    private final ScaffoldingLogPositionDTOMapper scaffoldingLogPositionDTOMapper;

    private final ScaffoldingLogService scaffoldingLogService;
    private final ScaffoldingLogDTOMapper scaffoldingLogDTOMapper;

    private final ScaffoldingLogPositionDimensionService scaffoldingLogPositionDimensionService;
    private final ScaffoldingLogPositionDimensionDTOMapper scaffoldingLogPositionDimensionDTOMapper;

    private final ScaffoldingLogPositionWorkingTimeService scaffoldingLogPositionWorkingTimeService;
    private final ScaffoldingLogPositionWorkingTimeDTOMapper scaffoldingLogPositionWorkingTimeDTOMapper;

    public ScaffoldingLogPositionServiceImpl(ScaffoldingLogPositionDao scaffoldingLogPositionDao, ScaffoldingLogPositionDTOMapper scaffoldingLogPositionDTOMapper, ScaffoldingLogService scaffoldingLogService, ScaffoldingLogDTOMapper scaffoldingLogDTOMapper, ScaffoldingLogPositionDimensionService scaffoldingLogPositionDimensionService, ScaffoldingLogPositionDimensionDTOMapper scaffoldingLogPositionDimensionDTOMapper, ScaffoldingLogPositionWorkingTimeService scaffoldingLogPositionWorkingTimeService, ScaffoldingLogPositionWorkingTimeDTOMapper scaffoldingLogPositionWorkingTimeDTOMapper) {
        this.scaffoldingLogPositionDao = scaffoldingLogPositionDao;
        this.scaffoldingLogPositionDTOMapper = scaffoldingLogPositionDTOMapper;
        this.scaffoldingLogService = scaffoldingLogService;
        this.scaffoldingLogDTOMapper = scaffoldingLogDTOMapper;
        this.scaffoldingLogPositionDimensionService = scaffoldingLogPositionDimensionService;
        this.scaffoldingLogPositionDimensionDTOMapper = scaffoldingLogPositionDimensionDTOMapper;
        this.scaffoldingLogPositionWorkingTimeService = scaffoldingLogPositionWorkingTimeService;
        this.scaffoldingLogPositionWorkingTimeDTOMapper = scaffoldingLogPositionWorkingTimeDTOMapper;
    }


    @Override
    public ScaffoldingLogPositionDTO save(ScaffoldingLogPositionDTO createDto) {
        log.debug("Request to save ScaffoldingLogPosition : {}", createDto.toString());
        //todo update parent position if exists and calculate full dimension of position.
        if (createDto.getParentPosition() != null) {
            //calculate full dimension - check workType for subtraction/addition particular dimensions
            List<ScaffoldingLogPositionDimensionBaseDTO> current = createDto.getParentPosition().getDimensions();
        }
        //todo save base position when we updated parent position if exists
        ScaffoldingLogPosition toSaveEntity = scaffoldingLogPositionDTOMapper.toEntity(createDto);
        ScaffoldingLogPosition savedEntity = scaffoldingLogPositionDao.save(toSaveEntity);
        ScaffoldingLogPositionDTO savedDTO = scaffoldingLogPositionDTOMapper.toDto(savedEntity);

        //todo save dimensions (new positionId must be present)
        createDto.getDimensions().forEach(positionDimensionBaseDTO -> {
            ScaffoldingLogPositionDimensionDTO toSave = ScaffoldingLogPositionDimensionDTO.builder()
                    .company(savedDTO.getCompany())
                    .dimensionType(positionDimensionBaseDTO.getDimensionType())
                    .height(positionDimensionBaseDTO.getHeight())
                    .width(positionDimensionBaseDTO.getWidth())
                    .length(positionDimensionBaseDTO.getLength())
                    .operationType(positionDimensionBaseDTO.getOperationType())
                    .dismantlingDate(positionDimensionBaseDTO.getDismantlingDate())
                    .assemblyDate(positionDimensionBaseDTO.getAssemblyDate())
                    .build();
            ScaffoldingLogPositionDimensionDTO savedDimension = scaffoldingLogPositionDimensionService.save(toSave);
        });

        //todo save working times (new positionId must be present)
        createDto.getWorkingTimes().forEach(positionWorkingTimeDTO -> {
            ScaffoldingLogPositionWorkingTimeDTO toSave = ScaffoldingLogPositionWorkingTimeDTO.builder()
                    .company(savedDTO.getCompany())
                    .operationType(positionWorkingTimeDTO.getOperationType())
                    .numberOfHours(positionWorkingTimeDTO.getNumberOfHours())
                    .numberOfWorkers(positionWorkingTimeDTO.getNumberOfWorkers()).build();
            ScaffoldingLogPositionWorkingTimeDTO savedWorkingTime = scaffoldingLogPositionWorkingTimeService.save(toSave);
        });


        return null;
    }

    @Override
    public ScaffoldingLogPositionDTO update(ScaffoldingLogPositionDTO updateDto) {
        return null;
    }

    @Override
    public ScaffoldingLogPositionDTO findById(Long aLong) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }

    private BigDecimal calculateScaffoldingDimensions(List<ScaffoldingLogPositionDimensionBaseDTO> dimensionDTOList) {
        return BigDecimal.ZERO;
    }
}
