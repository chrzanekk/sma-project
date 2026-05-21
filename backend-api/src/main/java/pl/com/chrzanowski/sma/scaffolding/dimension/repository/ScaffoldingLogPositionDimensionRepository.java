package pl.com.chrzanowski.sma.scaffolding.dimension.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;

import java.util.List;

public interface ScaffoldingLogPositionDimensionRepository extends JpaRepository<ScaffoldingLogPositionDimension, Long>,
        JpaSpecificationExecutor<ScaffoldingLogPositionDimension>,
        QuerydslPredicateExecutor<ScaffoldingLogPositionDimension> {

    List<ScaffoldingLogPositionDimension> findByScaffoldingPositionId(Long id);
}
