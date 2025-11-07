package pl.com.chrzanowski.sma.scaffolding.worktype.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.WorkTypeException;
import pl.com.chrzanowski.sma.common.exception.error.WorkTypeErrorCode;
import pl.com.chrzanowski.sma.scaffolding.worktype.dao.WorkTypeDao;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.mapper.WorkTypeDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.worktype.model.WorkType;

import java.util.Optional;

@Service
@Transactional
public class WorkTypeServiceImpl implements WorkTypeService {

    private static final Logger log = LoggerFactory.getLogger(WorkTypeServiceImpl.class);

    private final WorkTypeDao workTypeDao;
    private final WorkTypeDTOMapper workTypeDTOMapper;

    public WorkTypeServiceImpl(WorkTypeDao workTypeDao, WorkTypeDTOMapper workTypeDTOMapper) {
        this.workTypeDao = workTypeDao;
        this.workTypeDTOMapper = workTypeDTOMapper;
    }


    @Override
    public WorkTypeDTO save(WorkTypeDTO createDto) {
        log.debug("Request to save WorkType : {}", createDto.getId());
        WorkType workType = workTypeDTOMapper.toEntity(createDto);
        WorkType savedWorkType = workTypeDao.save(workType);
        return workTypeDTOMapper.toDto(savedWorkType);
    }

    @Override
    public WorkTypeDTO update(WorkTypeDTO updateDto) {
        log.debug("Request to update WorkType : {}", updateDto.getId());
        WorkType existingWorkType = workTypeDao.findById(updateDto.getId()).orElseThrow(() -> new WorkTypeException(WorkTypeErrorCode.WORK_TYPE_NOT_FOUND, "WorkType with id " + updateDto.getId() + " not found!"));
        workTypeDTOMapper.updateFromDto(updateDto, existingWorkType);
        WorkType updatedWorkType = workTypeDao.save(existingWorkType);
        return workTypeDTOMapper.toDto(updatedWorkType);
    }

    @Override
    public WorkTypeDTO findById(Long aLong) {
        log.debug("Request to get WorkType : {}", aLong);
        Optional<WorkType> workType = workTypeDao.findById(aLong);
        return workTypeDTOMapper.toDto(workType.orElseThrow(() -> new WorkTypeException(WorkTypeErrorCode.WORK_TYPE_NOT_FOUND, "WorkType with id " + aLong + " not found!")));
    }

    @Override
    public void delete(Long aLong) {
        log.debug("Request to delete WorkType : {}", aLong);
        if (workTypeDao.findById(aLong).isEmpty()) {
            throw new WorkTypeException(WorkTypeErrorCode.WORK_TYPE_NOT_FOUND, "WorkType with id " + aLong + " not found!");
        }
        workTypeDao.deleteById(aLong);
    }
}
