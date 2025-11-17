package pl.com.chrzanowski.sma.scaffolding.dimension.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;
import pl.com.chrzanowski.sma.scaffolding.dimension.repository.ScaffoldingLogPositionDimensionRepository;
import pl.com.chrzanowski.sma.scaffolding.dimension.service.filter.ScaffoldingLogPositionDimensionQuerySpec;

import java.util.List;
import java.util.Optional;

@Repository("scaffoldingLogPositionDimensionJPA")
public class ScaffoldingLogPositionDimensionDaoImpl implements ScaffoldingLogPositionDimensionDao {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionDimensionDaoImpl.class);

    private final ScaffoldingLogPositionDimensionRepository repository;
    private final ScaffoldingLogPositionDimensionQuerySpec querySpec;

    public ScaffoldingLogPositionDimensionDaoImpl(ScaffoldingLogPositionDimensionRepository repository, ScaffoldingLogPositionDimensionQuerySpec querySpec) {
        this.repository = repository;
        this.querySpec = querySpec;
    }

    @Override
    public ScaffoldingLogPositionDimension save(ScaffoldingLogPositionDimension entity) {
        log.debug("DAO: Save dimension: {}", entity.toString());
        return repository.save(entity);
    }

    @Override
    public Optional<ScaffoldingLogPositionDimension> findById(Long aLong) {
        log.debug("DAO: Find dimension by id: {}", aLong);
        return repository.findById(aLong);
    }

    @Override
    public List<ScaffoldingLogPositionDimension> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find dimension by specification: {}", specification);
        return querySpec.buildQuery(specification, null).fetch();
    }

    @Override
    public Page<ScaffoldingLogPositionDimension> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Find dimension by specification with page: {}", specification);
        BlazeJPAQuery<ScaffoldingLogPositionDimension> baseQuery = querySpec.buildQuery(specification, pageable);
        PagedList<ScaffoldingLogPositionDimension> list = baseQuery.fetchPage((int) pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(list, pageable, list.getTotalSize());
    }

    @Override
    public void deleteById(Long aLong) {
        log.debug("DAO: Delete dimension by id: {}", aLong);
        repository.deleteById(aLong);
    }
}
