package pl.com.chrzanowski.sma.position.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.position.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long>,
        JpaSpecificationExecutor<Position>,
        QuerydslPredicateExecutor<Position> {
}
