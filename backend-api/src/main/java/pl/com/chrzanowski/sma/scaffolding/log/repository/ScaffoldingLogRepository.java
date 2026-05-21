package pl.com.chrzanowski.sma.scaffolding.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;

public interface ScaffoldingLogRepository extends JpaRepository<ScaffoldingLog, Long>,
        JpaSpecificationExecutor<ScaffoldingLog>,
        QuerydslPredicateExecutor<ScaffoldingLog> {
}
