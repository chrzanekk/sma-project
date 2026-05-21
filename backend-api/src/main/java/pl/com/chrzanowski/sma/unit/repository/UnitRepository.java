package pl.com.chrzanowski.sma.unit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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

    /**
     * Znajduje wszystkie globalne jednostki (company_id IS NULL)
     * Wynik jest cachowany dla lepszej wydajności
     */
    @Query("SELECT u FROM Unit u WHERE u.company IS NULL ORDER BY u.symbol")
    List<Unit> findAllGlobalUnits();

    /**
     * Znajduje globalną jednostkę po symbolu (company_id IS NULL)
     */
    @Query("SELECT u FROM Unit u WHERE u.company IS NULL AND u.symbol = :symbol")
    Optional<Unit> findGlobalUnitBySymbol(String symbol);

    /**
     * Znajduje globalne jednostki po typie
     */
    @Query("SELECT u FROM Unit u WHERE u.company IS NULL AND u.unitType = :unitType")
    List<Unit> findGlobalUnitsByType(UnitType unitType);

}
