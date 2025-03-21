package pl.com.chrzanowski.sma.common.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueryService<T, F> {
    List<T> findByFilter(F filter);

    Page<T> findByFilter(F filter, Pageable pageable);
}
