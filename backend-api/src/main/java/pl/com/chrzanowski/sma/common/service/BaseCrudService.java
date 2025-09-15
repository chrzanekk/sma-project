package pl.com.chrzanowski.sma.common.service;

public interface BaseCrudService<RD, CD, UD extends HasId<ID>, ID> {
    RD save(CD createDto);

    RD update(UD updateDto);

    RD findById(ID id);

    void delete(ID id);
}