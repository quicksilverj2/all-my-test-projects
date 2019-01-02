package com.smu.stakeme.component;

import com.smu.stakeme.util.StakeAppHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jitheshrajan on 12/5/18.
 */
@Component
public class FileLoadStartupRunner implements ApplicationRunner {


    @Value("${file.user}")
    private String userFile;

    @Value("${file.countryDetails}")
    private String countryDetailsFile;


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        System.out.println("Application starting!");
        String strArgs = Arrays.stream(applicationArguments.getSourceArgs()).collect(Collectors.joining("|"));
        System.out.println("Application started with arguments:" + strArgs);

        List<String> countryLines = StakeAppHelper.readEntireFile(countryDetailsFile);
        countryLines.stream().forEach(s -> StakeAppHelper.addCountryDetailsFromLine(s));

        List<String> fileLines = StakeAppHelper.readEntireFile(userFile);
        fileLines.stream().forEach(s -> StakeAppHelper.addStakeUserFromFileLine(s));




    }
}
