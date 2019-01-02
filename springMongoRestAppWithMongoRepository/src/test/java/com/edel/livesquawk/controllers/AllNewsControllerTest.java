package com.edel.livesquawk.controllers;

import com.edel.livesquawk.application.Application;
import com.edel.livesquawk.common.MongoDBConfiguration;
import com.edel.livesquawk.dao.SingleNewsItem;
import com.edel.livesquawk.mongorepos.BlockDealsRepository;
import com.edel.livesquawk.mongorepos.GeneralNewsRepository;
import com.edel.livesquawk.mongorepos.RawFeedInputRepository;
import com.edel.livesquawk.mongorepos.ResultsRepository;
import com.google.gson.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jitheshrajan on 12/4/18.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = AllNewsController.class, secure = false)
@ContextConfiguration(classes = {Application.class, MongoDBConfiguration.class})
public class AllNewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    GeneralNewsRepository generalNewsRepository;
    @MockBean
    BlockDealsRepository blockDealsRepository;
    @MockBean
    ResultsRepository resultsRepository;
    @MockBean
    RawFeedInputRepository rawFeedInputRepository;


    String singleNewsItemJson = "{\"title\" : \"LOANS, DEPOSITS OF INDIANS IN SWISS BANKS FELL 34.5% IN 2017\",\"description\" : \"LOANS, DEPOSITS OF INDIANS IN SWISS BANKS FELL 34.5% IN 2017\",\"guid\" : \"5b56e968e0e6825fad08408d\",\"category\" : \"Fixed_income\",\"date\" : \"2018-07-24T08:55:04Z\",\"custom_elements\" : [ ]}";


    @Before
    public void setup(){

        System.out.println("Before test methods are executed!");
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime();
            }
        }).create();

        SingleNewsItem singleNewsItem = gson.fromJson(singleNewsItemJson, SingleNewsItem.class);

        List<SingleNewsItem> singleNewsItemList = Arrays.asList(singleNewsItem);

//        LocalDateTime date = Mockito.mock(LocalDateTime.class);

        Mockito.when(generalNewsRepository.getNewsForSymbol(
                0,
                null,
                "11536_NSE"))
                .thenReturn(singleNewsItemList);

    }

    @Test
    public void searchAllNewsBySymbol() throws Exception {


        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/allNews/search?query=11536_NSE").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
    }
}
