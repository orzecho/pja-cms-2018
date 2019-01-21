package pl.edu.pja.nyan.service.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @param <D> - DTO type parameter.
 * @param <E> - Entity type parameter.
 */

public interface EntityMapper <D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    default List <E> toEntity(Collection<D> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    };

    default List <D> toDto(Collection<E> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    };
}
