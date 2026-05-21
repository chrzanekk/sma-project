package pl.com.chrzanowski.sma.scaffolding.position.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;

import java.util.List;
import java.util.Optional;

public interface ScaffoldingLogPositionRepository extends JpaRepository<ScaffoldingLogPosition, Long>,
        JpaSpecificationExecutor<ScaffoldingLogPosition>,
        QuerydslPredicateExecutor<ScaffoldingLogPosition> {

    Boolean existsByScaffoldingNumberAndScaffoldingLogId(String scaffoldingNumber, Long scaffoldingLogId);

    @EntityGraph(attributePaths = {"parentPosition"})
    boolean existsById(Long id);

    @Query("SELECT s FROM ScaffoldingLogPosition s WHERE s.id = :rootId OR s.parentPosition.id = :rootId")
    List<ScaffoldingLogPosition> findFamilyPositions(@Param("rootId") Long rootId);

    @Query(value = """
                SELECT * FROM scaffolding_log_position
                WHERE scaffolding_log_id = :logId
                  AND scaffolding_parent_id IS NULL
                  AND EXTRACT(YEAR FROM create_date) = :year
                ORDER BY create_date
                LIMIT 1
            """, nativeQuery = true)
    Optional<ScaffoldingLogPosition> findLatestRootPositionByLogAndYear(
            @Param("logId") Long logId,
            @Param("year") Integer year
    );
}
