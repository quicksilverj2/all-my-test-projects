package com.smu.stakeme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@SpringBootApplication(scanBasePackages = "com.smu.stakeme.controllers")
//@EnableMongoRepositories(basePackages = "com.smu.stakeme.mongorepos")

@SpringBootApplication
public class StakemeApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(StakemeApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(StakemeApplication.class, args);
	}
}
