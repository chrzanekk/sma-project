package pl.com.chrzanowski.sma.scaffolding.position.dao;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
        return null;
    }

    @Override
    public Optional<ScaffoldingLogPosition> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<ScaffoldingLogPosition> findAll(BooleanBuilder specification) {
        return List.of();
    }

    @Override
    public Page<ScaffoldingLogPosition> findAll(BooleanBuilder specification, Pageable pageable) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }
}
