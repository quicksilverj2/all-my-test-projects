package com.edel.livesquawk.controllers;

import com.edel.livesquawk.common.Utils;
import com.edel.livesquawk.mongomodels.GeneralNews;
import com.edel.livesquawk.mongorepos.GeneralNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static com.edel.livesquawk.application.AppConstants.PAGE_SIZE;

/**
 * Created by iqbal on 5/25/18.
 */

@RestController
@RequestMapping("/generalNews")
public class GeneralNewsController {
    @Autowired
    GeneralNewsRepository generalNewsRepository;

    @GetMapping("/all")
    public Page<GeneralNews> getAllGeneralNews(@RequestParam(value = "page", required = false) String page) {
        int pageNum = Utils.getPageNum(page);
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE, sort);
        return this.generalNewsRepository.findAll(pageable);
    }

    @GetMapping("/onOrBefore")
    public Page<GeneralNews> getGeneralNewsBefore(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd'T'H:m:s") LocalDateTime date
    ) {
        int pageNum = Utils.getPageNum(page);
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE, sort);

        Query query = new Query(Criteria.where("date").lte(date)).with(pageable);

        return this.generalNewsRepository.findByCustomQuery(query, pageable);
    }

    @GetMapping("/search")
    public Page<GeneralNews> searchGeneralNews(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam("keyword") String keyword
    ) {
        int pageNum = Utils.getPageNum(page);
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE, sort);
        return this.generalNewsRepository.findByTitleContainsIgnoreCaseOrderByDateDesc(keyword, pageable);
    }

    @GetMapping("/{newsCategory}")
    public Page<GeneralNews> getGeneralNewsForCategory(
            @PathVariable("newsCategory") String newsCategory,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'H:m:s") LocalDateTime date
    ) {
        int pageNum = Utils.getPageNum(page);
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE, sort);

        Query query = new Query().with(pageable);

        if (date != null) {
            query.addCriteria(
                    new Criteria().andOperator(
                            Criteria.where("date").lte(date),
                            Criteria.where("category").is(newsCategory)
                    )
            );
        } else {
            query.addCriteria(Criteria.where("category").is(newsCategory));
        }

        return this.generalNewsRepository.findByCustomQuery(query, pageable);
    }
}
