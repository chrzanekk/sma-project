package pl.com.chrzanowski.sma.common.dao;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BaseCrudDao<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll(BooleanBuilder specification);

    Page<T> findAll(BooleanBuilder specification, Pageable pageable);

    void deleteById(ID id);
}
