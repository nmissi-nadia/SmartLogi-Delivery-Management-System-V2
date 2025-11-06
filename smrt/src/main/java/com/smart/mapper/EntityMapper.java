package com.smart.mapper;

import java.util.List;

/**
 * A generic mapper interface for converting between DTOs (Data Transfer Objects) and Entities.
 * @param <D> - DTO type
 * @param <E> - Entity type
 */
public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);
}