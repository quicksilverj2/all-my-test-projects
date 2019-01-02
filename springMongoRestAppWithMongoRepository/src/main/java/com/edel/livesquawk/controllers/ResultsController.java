package com.edel.livesquawk.controllers;

import com.edel.livesquawk.common.Utils;
import com.edel.livesquawk.dao.ResultsResponseDao;
import com.edel.livesquawk.dao.SingleSymbolNewsDao;
import com.edel.livesquawk.mongorepos.ResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Created by iqbal on 5/28/18.
 */

@RestController
@RequestMapping("/results")
public class ResultsController {
    @Autowired
    ResultsRepository resultsRepository;

    @GetMapping("/all")
    public ResultsResponseDao getAllResults(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'H:m:s") LocalDateTime date
    ) {
        int pageNum = Utils.getPageNum(page);
        return this.resultsRepository.aggregateOnSymbol(pageNum, date, null);
    }

    @GetMapping("/search")
    public ResultsResponseDao getResultsByKeywordSearch(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'H:m:s") LocalDateTime date,
            @RequestParam("keyword") String keyword
    ) {
        int pageNum = Utils.getPageNum(page);
        return this.resultsRepository.aggregateOnSymbol(pageNum, date, keyword);
    }

    @GetMapping("/{symbol}")
    public SingleSymbolNewsDao getResultsForSymbol(
            @PathVariable("symbol") String symbol,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'H:m:s") LocalDateTime date
    ) {
        int pageNum = Utils.getPageNum(page);
        return this.resultsRepository.getSingleSymbolResults(pageNum, date, symbol);
    }
}
