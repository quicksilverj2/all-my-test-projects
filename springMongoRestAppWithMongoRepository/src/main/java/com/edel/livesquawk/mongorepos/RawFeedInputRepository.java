package com.edel.livesquawk.mongorepos;

import com.edel.livesquawk.common.ReadOnlyRepository;
import com.edel.livesquawk.mongomodels.RawFeedInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Iqbal on 5/14/2018.
 */

@RepositoryRestResource(collectionResourceRel = "raw-feed", path = "raw-feed")
public interface RawFeedInputRepository extends ReadOnlyRepository<RawFeedInput, String> {
    Page<RawFeedInput> findByTitleContainsIgnoreCase(@Param("keyword") String keyword, Pageable pageable);
}
