package pl.com.chrzanowski.sma.scaffolding.counter.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface ScaffoldingNumberCounterCustomRepository {
    Integer generateNextBaseNumber(Long logId, Integer year);
}
