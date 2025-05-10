package com.regisx001.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		// Force Flyway to treat PostgreSQL 17 as version 15
		// System.setProperty("flyway.database.postgresql.compatible-version", "15");

		SpringApplication.run(BlogApplication.class, args);
	}

}
