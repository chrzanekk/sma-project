package pl.com.chrzanowski.sma.common.service;

public interface BaseCrudService<T, ID> {
    T save(T t);

    T update(T t);

    T findById(ID id);

    void delete(ID id);
}
