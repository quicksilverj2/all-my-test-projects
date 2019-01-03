package com.edel.rulestore;

import com.edel.rulestore.mongo.ExpressionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.edel.rulestore")
@EnableMongoRepositories(basePackageClasses = { ExpressionRepository.class})
public class RulestoreApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RulestoreApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(RulestoreApplication.class, args);
	}
}
