package com.mmt.moviebooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.mmt.moviebooking.controllers")
@EnableMongoRepositories(basePackages = "com.mmt.moviebooking.mongorepos")
public class MoviebookingApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MoviebookingApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MoviebookingApplication.class, args);
	}
}

