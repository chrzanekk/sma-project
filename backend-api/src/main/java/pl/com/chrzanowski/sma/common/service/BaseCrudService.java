package pl.com.chrzanowski.sma.common.service;

import java.util.List;

public interface BaseCrudService<T, ID> {
    T save(T t);

    T update(T t);

    T findById(ID id);

    List<T> findAll();

    void delete(ID id);
}
