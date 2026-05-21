package pl.com.chrzanowski.sma.scaffolding.counter.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ScaffoldingNumberCounterCustomRepositoryImpl implements ScaffoldingNumberCounterCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Integer generateNextBaseNumber(Long logId, Integer year) {
        String sql = """
                INSERT INTO scaffolding_number_counter (scaffolding_log_id, year, last_number)
                VALUES (:logId, :year, 1)
                ON CONFLICT (scaffolding_log_id, year)
                DO UPDATE SET last_number = scaffolding_number_counter.last_number + 1
                RETURNING last_number
                """;

        Query query = entityManager.createNativeQuery(sql)
                .setParameter("logId", logId)
                .setParameter("year", year);

        // Używamy getSingleResult() a nie executeUpdate()!
        // Dzięki temu obsługujemy to co oddaje klauzula RETURNING
        return (Integer) query.getSingleResult();
    }
}
