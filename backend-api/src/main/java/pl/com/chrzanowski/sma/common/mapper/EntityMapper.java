package pl.com.chrzanowski.sma.common.mapper;

import java.util.List;
import java.util.Set;

public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntityList(List<D> dtoList);

    List<D> toDtoList(List<E> entityList);

    Set<E> toEntitySet(Set<D> dtoSet);

    Set<D> toDtoSet(Set<E> entitySet);

}
