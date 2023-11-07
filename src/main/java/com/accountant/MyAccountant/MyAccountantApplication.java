package com.accountant.MyAccountant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;

@SpringBootApplication
@Async
public class MyAccountantApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyAccountantApplication.class, args);
	}

}
