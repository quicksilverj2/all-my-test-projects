package com.edel.livesquawk.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Created by Iqbal on 5/15/2018.
 */


@NoRepositoryBean
public interface ReadOnlyRepository<T,ID> extends Repository<T, ID> {
    // From CrudRepository
    Optional<T> findById(ID var1);
    boolean existsById(ID var1);
    Iterable<T> findAll();
    Iterable<T> findAllById(Iterable<ID> var1);
    long count();

    // From PagingAndSortingRepository
    Iterable<T> findAll(Sort var1);
    Page<T> findAll(Pageable var1);
}
