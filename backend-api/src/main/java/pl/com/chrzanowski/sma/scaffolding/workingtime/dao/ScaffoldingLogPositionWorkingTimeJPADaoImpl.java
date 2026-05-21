package pl.com.chrzanowski.sma.scaffolding.workingtime.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;
import pl.com.chrzanowski.sma.scaffolding.workingtime.repository.ScaffoldingLogPositionWorkingTimeRepository;
import pl.com.chrzanowski.sma.scaffolding.workingtime.service.filter.ScaffoldingLogPositionWorkingTimeQuerySpec;

import java.util.List;
import java.util.Optional;

@Repository("scaffoldingLogPositionWorkingTimeJPA")
public class ScaffoldingLogPositionWorkingTimeJPADaoImpl implements ScaffoldingLogPositionWorkingTimeDao {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionWorkingTimeJPADaoImpl.class);

    private final ScaffoldingLogPositionWorkingTimeRepository repository;
    private final ScaffoldingLogPositionWorkingTimeQuerySpec querySpec;

    public ScaffoldingLogPositionWorkingTimeJPADaoImpl(ScaffoldingLogPositionWorkingTimeRepository repository, ScaffoldingLogPositionWorkingTimeQuerySpec querySpec) {
        this.repository = repository;
        this.querySpec = querySpec;
    }

    @Override
    public ScaffoldingLogPositionWorkingTime save(ScaffoldingLogPositionWorkingTime entity) {
        log.debug("DAO: Save workingTime: {}", entity.toString());
        return repository.save(entity);
    }

    @Override
    public Optional<ScaffoldingLogPositionWorkingTime> findById(Long aLong) {
        log.debug("DAO: findById: {}", aLong);
        return repository.findById(aLong);
    }

    @Override
    public List<ScaffoldingLogPositionWorkingTime> findAll(BooleanBuilder specification) {
        log.debug("DAO: find all workingTimes by specification: {}", specification);
        return querySpec.buildQuery(specification, null).fetch();
    }

    @Override
    public Page<ScaffoldingLogPositionWorkingTime> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: find all workingTimes by specification with page: {}", specification);
        BlazeJPAQuery<ScaffoldingLogPositionWorkingTime> baseQuery = querySpec.buildQuery(specification, pageable);
        PagedList<ScaffoldingLogPositionWorkingTime> content = baseQuery.fetchPage((int) pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(content, pageable, content.getTotalSize());
    }

    @Override
    public void deleteById(Long aLong) {
        log.debug("DAO: deleteById: {}", aLong);
        repository.deleteById(aLong);
    }
}
