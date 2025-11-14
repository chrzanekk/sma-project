package pl.com.chrzanowski.sma.scaffolding.log.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.ScaffoldingLogException;
import pl.com.chrzanowski.sma.common.exception.error.ScaffoldingLogErrorCode;
import pl.com.chrzanowski.sma.scaffolding.log.dao.ScaffoldingLogDao;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogDTO;
import pl.com.chrzanowski.sma.scaffolding.log.mapper.ScaffoldingLogDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;

import java.util.Optional;

@Service
@Transactional
public class ScaffoldingLogServiceImpl implements ScaffoldingLogService {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogServiceImpl.class);

    private final ScaffoldingLogDao scaffoldingLogDao;
    private final ScaffoldingLogDTOMapper scaffoldingLogDTOMapper;

    public ScaffoldingLogServiceImpl(ScaffoldingLogDao scaffoldingLogDao, ScaffoldingLogDTOMapper scaffoldingLogDTOMapper) {
        this.scaffoldingLogDao = scaffoldingLogDao;
        this.scaffoldingLogDTOMapper = scaffoldingLogDTOMapper;
    }

    @Override
    public ScaffoldingLogDTO save(ScaffoldingLogDTO createDto) {
        log.debug("Request to save ScaffoldingLog : {}", createDto.toString());
        ScaffoldingLog scaffoldingLog = scaffoldingLogDTOMapper.toEntity(createDto);
        ScaffoldingLog saved = scaffoldingLogDao.save(scaffoldingLog);
        return scaffoldingLogDTOMapper.toDto(saved);
    }

    @Override
    public ScaffoldingLogDTO update(ScaffoldingLogDTO updateDto) {
        log.debug("Request to update ScaffoldingLog : {}", updateDto.getId());
        ScaffoldingLog existing = scaffoldingLogDao.findById(updateDto.getId())
                .orElseThrow(() -> new ScaffoldingLogException(ScaffoldingLogErrorCode.SCAFFOLDING_LOG_NOT_FOUND, "Scaffolding Log " + updateDto.getId() + " not found."));
        scaffoldingLogDTOMapper.updateFromDto(updateDto, existing);
        ScaffoldingLog updated = scaffoldingLogDao.save(existing);
        return scaffoldingLogDTOMapper.toDto(updated);
    }

    @Override
    public ScaffoldingLogDTO findById(Long aLong) {
        log.debug("Request to get ScaffoldingLog by id: {}", aLong);
        Optional<ScaffoldingLog> result = scaffoldingLogDao.findById(aLong);
        return scaffoldingLogDTOMapper.toDto(result.orElseThrow(
                () -> new ScaffoldingLogException(ScaffoldingLogErrorCode.SCAFFOLDING_LOG_NOT_FOUND, "Scaffolding Log " + aLong + " not found.")
        ));
    }

    @Override
    public void delete(Long aLong) {
        log.debug("Request to delete ScaffoldingLog by id: {}", aLong);
        if (scaffoldingLogDao.findById(aLong).isEmpty()) {
            throw new ScaffoldingLogException(ScaffoldingLogErrorCode.SCAFFOLDING_LOG_NOT_FOUND, "Scaffolding Log " + aLong + " not found.");
        }
        scaffoldingLogDao.deleteById(aLong);
    }
}
