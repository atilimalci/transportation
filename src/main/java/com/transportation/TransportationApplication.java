package com.transportation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TransportationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransportationApplication.class, args);
	}

}
