package pl.com.chrzanowski.sma.scaffolding.position.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.position.repository.ScaffoldingLogPositionRepository;
import pl.com.chrzanowski.sma.scaffolding.position.service.filter.ScaffoldingLogPositionQuerySpec;

import java.util.List;
import java.util.Optional;

@Repository("scaffoldingLogPositionJPA")
public class ScaffoldingLogPositionDaoImpl implements ScaffoldingLogPositionDao {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionDaoImpl.class);

    private final ScaffoldingLogPositionRepository repository;
    private final ScaffoldingLogPositionQuerySpec querySpec;

    public ScaffoldingLogPositionDaoImpl(ScaffoldingLogPositionRepository repository, ScaffoldingLogPositionQuerySpec querySpec) {
        this.repository = repository;
        this.querySpec = querySpec;
    }

    @Override
    public ScaffoldingLogPosition save(ScaffoldingLogPosition entity) {
        log.debug("DAO: Save ScaffoldingLogPosition: {}", entity.toString());
        return repository.save(entity);
    }

    @Override
    public Optional<ScaffoldingLogPosition> findById(Long aLong) {
        log.debug("DAO: Find ScaffoldingLogPosition by id: {}", aLong);
        return repository.findById(aLong);
    }

    @Override
    public List<ScaffoldingLogPosition> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find ScaffoldingLogPosition by specification: {}", specification);
        return querySpec.buildQuery(specification, null).fetch();
    }

    @Override
    public Page<ScaffoldingLogPosition> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Find ScaffoldingLogPosition by specification with page: {}", specification);
        BlazeJPAQuery<ScaffoldingLogPosition> baseQuery = querySpec.buildQuery(specification, pageable);
        PagedList<ScaffoldingLogPosition> list = baseQuery.fetchPage(Math.toIntExact(pageable.getOffset()), pageable.getPageSize());
        return new PageImpl<>(list, pageable, list.getTotalSize());
    }

    @Override
    public void deleteById(Long aLong) {
        log.debug("DAO: Delete ScaffoldingLogPosition by id: {}", aLong);
        repository.deleteById(aLong);
    }
}
