package com.basic.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.basic.security.repository.UserRepository;

// https://github.com/koushikkothagal/spring-security-jwt

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class )
public class SpringBasicSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBasicSecurityApplication.class, args);
	}

}
