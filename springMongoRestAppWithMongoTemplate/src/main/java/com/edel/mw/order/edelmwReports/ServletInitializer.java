package com.edel.mw.order.edelmwReports;

import com.edel.mw.order.edelmwReports.application.EdelmwReportsApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EdelmwReportsApplication.class);
	}

}
