package com.tinqinacademy.auth.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.tinqinacademy.auth")
//@EntityScan(basePackages = "com.tinqinacademy.auth.persistence.models")
//@EnableJpaRepositories(basePackages = "com.tinqinacademy.auth.persistence.repository")
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
