package pl.com.chrzanowski.sma.scaffolding.workingtime.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.ScaffoldingLogPositionWorkingTimeException;
import pl.com.chrzanowski.sma.common.exception.error.ScaffoldingLogPositionWorkingTimeErrorCode;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dao.ScaffoldingLogPositionWorkingTimeDao;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.mapper.ScaffoldingLogPositionWorkingTimeDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;

import java.util.Optional;

@Service
@Transactional
public class ScaffoldingLogPositionWorkingTimeServiceImpl implements ScaffoldingLogPositionWorkingTimeService {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionWorkingTimeServiceImpl.class);

    private final ScaffoldingLogPositionWorkingTimeDao dao;
    private final ScaffoldingLogPositionWorkingTimeDTOMapper dtoMapper;

    public ScaffoldingLogPositionWorkingTimeServiceImpl(ScaffoldingLogPositionWorkingTimeDao dao, ScaffoldingLogPositionWorkingTimeDTOMapper dtoMapper) {
        this.dao = dao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public ScaffoldingLogPositionWorkingTimeDTO save(ScaffoldingLogPositionWorkingTimeDTO createDto) {
        log.debug("Request to save ScaffoldingLogPositionWorkingTime : {}", createDto.getId());
        ScaffoldingLogPositionWorkingTime entity = dtoMapper.toEntity(createDto);
        ScaffoldingLogPositionWorkingTime result = dao.save(entity);
        return dtoMapper.toDto(result);
    }

    @Override
    public ScaffoldingLogPositionWorkingTimeDTO update(ScaffoldingLogPositionWorkingTimeDTO updateDto) {
        log.debug("Request to update ScaffoldingLogPositionWorkingTime : {}", updateDto.getId());
        ScaffoldingLogPositionWorkingTime existing = dao.findById(updateDto.getId()).orElseThrow(
                () -> new ScaffoldingLogPositionWorkingTimeException(ScaffoldingLogPositionWorkingTimeErrorCode.SCAFFOLDING_LOG_POSITION_WORKING_TIME_NOT_FOUND, "Working time with id " + updateDto.getId() + "not found."));
        dtoMapper.updateFromDto(updateDto, existing);
        ScaffoldingLogPositionWorkingTime result = dao.save(existing);
        return dtoMapper.toDto(result);
    }

    @Override
    public ScaffoldingLogPositionWorkingTimeDTO findById(Long aLong) {
        log.debug("Request to get ScaffoldingLogPositionWorkingTime by id: {}", aLong);
        Optional<ScaffoldingLogPositionWorkingTime> result = dao.findById(aLong);
        return dtoMapper.toDto(result.orElseThrow(
                () -> new ScaffoldingLogPositionWorkingTimeException(ScaffoldingLogPositionWorkingTimeErrorCode.SCAFFOLDING_LOG_POSITION_WORKING_TIME_NOT_FOUND, "Working time with id " + aLong + "not found.")
        ));
    }

    @Override
    public void delete(Long aLong) {
        log.debug("Request to delete ScaffoldingLogPositionWorkingTime : {}", aLong);
        if (dao.findById(aLong).isEmpty()) {
            throw new ScaffoldingLogPositionWorkingTimeException(ScaffoldingLogPositionWorkingTimeErrorCode.SCAFFOLDING_LOG_POSITION_WORKING_TIME_NOT_FOUND, "Working time with id " + aLong + "not found.");
        }
        dao.deleteById(aLong);
    }
}
