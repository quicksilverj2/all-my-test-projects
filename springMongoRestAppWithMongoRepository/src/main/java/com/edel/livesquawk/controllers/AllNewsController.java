package com.edel.livesquawk.controllers;

import com.edel.livesquawk.common.Utils;
import com.edel.livesquawk.dao.SingleNewsItem;
import com.edel.livesquawk.dao.SingleSymbolNewsDao;
import com.edel.livesquawk.mongorepos.BlockDealsRepository;
import com.edel.livesquawk.mongorepos.GeneralNewsRepository;
import com.edel.livesquawk.mongorepos.ResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edel.livesquawk.application.AppConstants.PAGE_SIZE;


/**
 * Created by Iqbal on 2018-06-19.
 */

@RestController
@RequestMapping("/allNews")
public class AllNewsController {
    @Autowired
    GeneralNewsRepository generalNewsRepository;

    @Autowired
    BlockDealsRepository blockDealsRepository;

    @Autowired
    ResultsRepository resultsRepository;


    @GetMapping("/search")
    public SingleSymbolNewsDao searchAllNewsBySymbol(
            @RequestParam("query") String symbol,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'H:m:s") LocalDateTime date
    ) {
        int pageNum = Utils.getPageNum(page);

        List<SingleNewsItem> generalNews =  this.generalNewsRepository.getNewsForSymbol(pageNum, date, symbol);
        List<SingleNewsItem> blockDealsNews =  this.blockDealsRepository.getNewsForSymbol(pageNum, date, symbol);
        List<SingleNewsItem> resultsNews =  this.resultsRepository.getNewsForSymbol(pageNum, date, symbol);

        List<SingleNewsItem> generalAndBlockNews = Stream.concat(generalNews.stream(), blockDealsNews.stream())
                .collect(Collectors.toList());

        List<SingleNewsItem> allNews = Stream.concat(generalAndBlockNews.stream(), resultsNews.stream())
                .collect(Collectors.toList());

        allNews.sort(Comparator.comparing(SingleNewsItem::getDate).reversed());

        int startIndex = PAGE_SIZE * pageNum;
        int endIndex = allNews.size() < (startIndex + PAGE_SIZE) ? allNews.size() : startIndex + PAGE_SIZE;

        return SingleSymbolNewsDao.builder()
                .symbol(symbol)
                .content(new ArrayList(allNews.subList(startIndex, endIndex)))
                .totalElements(allNews.size())
                .first(pageNum == 0)
                .last(endIndex == allNews.size())
                .totalPages((int) Math.ceil(allNews.size() / (float) PAGE_SIZE))
                .size(PAGE_SIZE)
                .number(pageNum)
                .build();
    }
}
