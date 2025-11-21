package pl.com.chrzanowski.sma.unit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.common.enumeration.UnitType;
import pl.com.chrzanowski.sma.unit.model.Unit;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long>,
        JpaSpecificationExecutor<Unit>,
        QuerydslPredicateExecutor<Unit> {

    Optional<Unit> findBySymbolAndCompanyId(String symbol, Long companyId);

    List<Unit> findByCompanyIdAndUnitType(Long companyId, UnitType unitType);

    List<Unit> findByCompanyId(Long companyId);
}
