package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	/**
	 *  TODO @ExceptionHandler for Controller
	 *  Maybe use jjwt-io token dependency instead com.auth0 jwt
	 *  Pagination
	 *  Циклические зависимости
	 *  LazyInitializationException
	 *  csrf cors?
	 *  привести в порядок web security config
	 *  рефакторинг
	 * **/
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
