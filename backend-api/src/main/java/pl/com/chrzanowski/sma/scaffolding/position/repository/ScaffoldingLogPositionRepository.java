package pl.com.chrzanowski.sma.scaffolding.position.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;

public interface ScaffoldingLogPositionRepository extends JpaRepository<ScaffoldingLogPosition, Long>,
        JpaSpecificationExecutor<ScaffoldingLogPosition>,
        QuerydslPredicateExecutor<ScaffoldingLogPosition> {

    Boolean existsByScaffoldingNumberAndScaffoldingLogId(String scaffoldingNumber, Long scaffoldingLogId);

    boolean existsById(Long id);
}
