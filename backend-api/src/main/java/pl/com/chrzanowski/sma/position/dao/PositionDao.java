package pl.com.chrzanowski.sma.position.dao;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.position.model.Position;

import java.util.List;
import java.util.Optional;

public interface PositionDao {

    Position save(Position position);

    Optional<Position> findById(Long id);

    Page<Position> findAll(BooleanBuilder specification, Pageable pageable);

    List<Position> findAll(BooleanBuilder specification);

    void deleteById(Long id);

}
