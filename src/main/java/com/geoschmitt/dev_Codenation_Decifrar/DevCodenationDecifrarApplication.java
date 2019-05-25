package com.geoschmitt.dev_Codenation_Decifrar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class DevCodenationDecifrarApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevCodenationDecifrarApplication.class, args);
	}

	
}
