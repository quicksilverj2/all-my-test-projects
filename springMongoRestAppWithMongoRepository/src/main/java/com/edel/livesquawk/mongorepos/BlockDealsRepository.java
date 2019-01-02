package com.edel.livesquawk.mongorepos;

import com.edel.livesquawk.common.ReadOnlyRepository;
import com.edel.livesquawk.mongomodels.BlockDeals;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * Created by Iqbal on 5/14/2018.
 */

public interface BlockDealsRepository extends ReadOnlyRepository<BlockDeals, String>, BlockDealsRepositoryCustom {
    Page<BlockDeals> findByTitleContainsIgnoreCaseOrderByDateDesc(@Param("keyword") String keyword, Pageable pageable);
}
