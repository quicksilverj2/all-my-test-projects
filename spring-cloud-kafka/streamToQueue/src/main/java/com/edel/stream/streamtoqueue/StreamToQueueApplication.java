package com.edel.stream.streamtoqueue;

import com.edel.stream.streamtoqueue.binding.QuoteStreamBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(QuoteStreamBinding.class)
@SpringBootApplication
public class StreamToQueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamToQueueApplication.class, args);
	}
}
