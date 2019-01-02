package com.edel.livesquawk.mongorepos;

import com.edel.livesquawk.common.ReadOnlyRepository;
import com.edel.livesquawk.mongomodels.GeneralNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * Created by Iqbal on 5/11/2018.
 */

//@RepositoryRestResource(collectionResourceRel = "general-news", path = "general-news")
public interface GeneralNewsRepository extends ReadOnlyRepository<GeneralNews, String>, GeneralNewsRepositoryCustom {
    // Example URI: general-news/search/findByDateBefore?date=2018-05-14T07:34:43&page=0&size=15
//    Page<GeneralNews> findByDateBeforeOrderByDateDesc(@Param("date") @DateTimeFormat(pattern="yyyy-MM-dd'T'H:m:s") LocalDateTime date, Pageable pageable);

    // Example URI: general-news/search/findByTitleContainsIgnoreCase?keyword=INDUSTRIAL%20output&page=0
    Page<GeneralNews> findByTitleContainsIgnoreCaseOrderByDateDesc(@Param("keyword") String keyword, Pageable pageable);

//    Page<GeneralNews> findAllBy(@Param("category") String category, Pageable pageable);
}
