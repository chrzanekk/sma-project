package pl.com.chrzanowski.sma.scaffolding.position.dao;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;

import java.util.List;
import java.util.Optional;

public class ScaffoldingLogPositionDaoImpl implements ScaffoldingLogPositionDao {
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
