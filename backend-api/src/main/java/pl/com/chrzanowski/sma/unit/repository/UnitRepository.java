package pl.com.chrzanowski.sma.unit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.unit.model.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long>,
        JpaSpecificationExecutor<Unit>,
        QuerydslPredicateExecutor<Unit> {
}
