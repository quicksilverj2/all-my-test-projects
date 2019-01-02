package com.edel.mw.order.edelmwReports.application;

import com.edel.mw.order.edelmwReports.v0.mongo.OrderBookRepository;
import com.edel.mw.order.edelmwReports.v1.mongo.OrderBookRepositoryV1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.edel.mw.order.edelmwReports")
@EnableMongoRepositories(basePackageClasses = { OrderBookRepository.class, OrderBookRepositoryV1.class})
public class EdelmwReportsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EdelmwReportsApplication.class, args);
	}


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EdelmwReportsApplication.class);
	}
}
