package pl.com.chrzanowski.sma.scaffolding.workingtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;

public interface ScaffoldingLogPositionWorkingTimeRepository extends JpaRepository<ScaffoldingLogPositionWorkingTime, Long>,
        JpaSpecificationExecutor<ScaffoldingLogPositionWorkingTime>,
        QuerydslPredicateExecutor<ScaffoldingLogPositionWorkingTime> {
}
