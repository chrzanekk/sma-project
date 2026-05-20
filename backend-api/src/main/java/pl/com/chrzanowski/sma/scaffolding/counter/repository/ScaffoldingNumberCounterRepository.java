package pl.com.chrzanowski.sma.scaffolding.counter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.scaffolding.counter.model.ScaffoldingNumberCounter;

@Repository
public interface ScaffoldingNumberCounterRepository extends JpaRepository<ScaffoldingNumberCounter, Long>,
        ScaffoldingNumberCounterCustomRepository {

    // 2. Samo podejrzenie numeru dla Frontendu (bez modyfikacji w bazie)
    @Query(value = """
            SELECT COALESCE(MAX(last_number), 0) + 1
            FROM scaffolding_number_counter
            WHERE scaffolding_log_id = :logId AND year = :year
            """, nativeQuery = true)
    Integer peekNextBaseNumber(@Param("logId") Long logId, @Param("year") Integer year);
}