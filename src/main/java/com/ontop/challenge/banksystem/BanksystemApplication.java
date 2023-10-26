package com.ontop.challenge.banksystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.ontop.challenge")
@EnableMongoRepositories
public class BanksystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanksystemApplication.class, args);
	}

}
