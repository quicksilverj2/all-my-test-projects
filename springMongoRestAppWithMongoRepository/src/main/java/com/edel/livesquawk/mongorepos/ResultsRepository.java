package com.edel.livesquawk.mongorepos;

import com.edel.livesquawk.common.ReadOnlyRepository;
import com.edel.livesquawk.mongomodels.Results;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * Created by Iqbal on 5/14/2018.
 */

public interface ResultsRepository extends ReadOnlyRepository<Results, String>, ResultsRepositoryCustom {
//    List<Results> findByDateBeforeOrderByDateDesc(@Param("date") @DateTimeFormat(pattern="yyyy-MM-dd'T'H:m:s") LocalDateTime date);
//    List<Results> findByTitleContainsIgnoreCaseOrderByDateDesc(@Param("keyword") String keyword);

    Page<Results> findByTitleContainsIgnoreCaseOrderByDateDesc(@Param("keyword") String keyword, Pageable pageable);
}
