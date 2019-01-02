package com.edel.livesquawk.controllers;

import com.edel.livesquawk.common.Utils;
import com.edel.livesquawk.mongomodels.BlockDeals;
import com.edel.livesquawk.mongorepos.BlockDealsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.edel.livesquawk.application.AppConstants.PAGE_SIZE;

/**
 * Created by iqbal on 5/28/18.
 */

@RestController
@RequestMapping("/blockDealsNews")
public class BlockDealsNewsController {
    @Autowired
    BlockDealsRepository blockDealsRepository;

    @GetMapping("/all")
    public Page<BlockDeals> getAllBlockDealsNews(@RequestParam(value = "page", required = false) String page) {
        int pageNum = Utils.getPageNum(page);
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE, sort);
        return this.blockDealsRepository.findAll(pageable);
    }

    @GetMapping("/onOrBefore")
    public Page<BlockDeals> getBlockDealsNewsBefore(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd'T'H:m:s") LocalDateTime date
    ) {
        int pageNum = Utils.getPageNum(page);
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE, sort);
        Query query = new Query(Criteria.where("date").lte(date)).with(pageable);
        return this.blockDealsRepository.findByDateOnOrBefore(query, pageable);
    }

    @GetMapping("/search")
    public Page<BlockDeals> searchBlockDealsNews(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam("keyword") String keyword
    ) {
        int pageNum = Utils.getPageNum(page);
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE, sort);
        return this.blockDealsRepository.findByTitleContainsIgnoreCaseOrderByDateDesc(keyword, pageable);
    }
}
