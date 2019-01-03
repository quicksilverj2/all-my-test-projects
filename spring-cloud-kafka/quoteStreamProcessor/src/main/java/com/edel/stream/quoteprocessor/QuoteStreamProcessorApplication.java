package com.edel.stream.quoteprocessor;

import com.edel.stream.quoteprocessor.binding.QuoteProcessorBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(QuoteProcessorBinding.class)
@SpringBootApplication
public class QuoteStreamProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuoteStreamProcessorApplication.class, args);
	}
}
