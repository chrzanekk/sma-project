package pl.com.chrzanowski.sma.scaffolding.log.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;
import pl.com.chrzanowski.sma.scaffolding.log.repository.ScaffoldingLogRepository;
import pl.com.chrzanowski.sma.scaffolding.log.service.filter.ScaffoldingLogQuerySpec;

import java.util.List;
import java.util.Optional;


@Repository("scaffoldingLogJPA")
public class ScaffoldingLogDaoImpl implements ScaffoldingLogDao {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogDaoImpl.class);

    private final ScaffoldingLogRepository scaffoldingLogRepository;
    private final ScaffoldingLogQuerySpec scaffoldingLogQuerySpec;

    public ScaffoldingLogDaoImpl(ScaffoldingLogRepository scaffoldingLogRepository, ScaffoldingLogQuerySpec scaffoldingLogQuerySpec) {
        this.scaffoldingLogRepository = scaffoldingLogRepository;
        this.scaffoldingLogQuerySpec = scaffoldingLogQuerySpec;
    }

    @Override
    public ScaffoldingLog save(ScaffoldingLog entity) {
        log.debug("DAO: Save ScaffoldingLog: {}", entity.getId());
        return scaffoldingLogRepository.save(entity);
    }

    @Override
    public Optional<ScaffoldingLog> findById(Long aLong) {
        log.debug("DAO: Find ScaffoldingLog by id: {}", aLong);
        return scaffoldingLogRepository.findById(aLong);
    }

    @Override
    public List<ScaffoldingLog> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find ScaffoldingLog by specification: {}", specification);
        return scaffoldingLogQuerySpec.buildQuery(specification, null).fetch();
    }

    @Override
    public Page<ScaffoldingLog> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Find ScaffoldingLog by specification with page: {}", specification);
        BlazeJPAQuery<ScaffoldingLog> baseQuery = scaffoldingLogQuerySpec.buildQuery(specification, pageable);
        PagedList<ScaffoldingLog> scaffoldingLogs = baseQuery.fetchPage((int) pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(scaffoldingLogs, pageable, scaffoldingLogs.getTotalSize());
    }

    @Override
    public void deleteById(Long aLong) {
        log.debug("DAO: Delete ScaffoldingLog by id: {}", aLong);
        scaffoldingLogRepository.deleteById(aLong);
    }
}
