package pl.com.chrzanowski.sma.scaffolding.worktype.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import pl.com.chrzanowski.sma.scaffolding.worktype.model.WorkType;

public interface WorkTypeRepository extends JpaRepository<WorkType, Long>,
        JpaSpecificationExecutor<WorkType>,
        QuerydslPredicateExecutor<WorkType> {

    void deleteById(@Param("id") Long id);
}
