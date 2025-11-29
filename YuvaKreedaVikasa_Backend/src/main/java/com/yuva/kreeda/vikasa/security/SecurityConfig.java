package com.yuva.kreeda.vikasa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // Disable CSRF for development/Postman testing
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll() // Allow all requests
        );
    return http.build();
  }
}

//The 401 Unauthorized error is happening because spring-boot-starter-security is included in your project, but there was no configuration to tell it to allow requests. By default, it locks everything down.
//
//I have created a new file: src/main/java/com/yuva/kreeda/vikasa/security/SecurityConfig.java. This configuration permits all requests and disables CSRF (which is common for development APIs).