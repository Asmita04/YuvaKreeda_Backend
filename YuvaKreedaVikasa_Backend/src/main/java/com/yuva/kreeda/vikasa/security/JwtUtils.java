package com.yuva.kreeda.vikasa.security;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yuva.kreeda.vikasa.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component // spring bean
public class JwtUtils {
  // injecting value of the property using SpEL (Spring expression Lanuage)
  @Value("${jwt.secret.key}")
  private String secretKey;
  // injecting value of the property using SpEL (Spring expression Lanuage)
  @Value("${jwt.expiration}")
  private long expTime;
  private SecretKey key;

  @PostConstruct
  public void myInit() {
    System.out.println("in init - ");
    System.out.println(expTime);
    // creates a symmetric secret key for - signing & verification
    key = Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  // generate JWT
  public String generateToken(Object principal) {
    // down cast from Object ---> User (Custom User Entity)
    // Note: The principal here will be our CustomUserDetails which wraps the User
    // entity,
    // or if we pass the User entity directly. Let's assume we pass the User entity
    // for simplicity based on the user's snippet,
    // but in Spring Security it's usually UserDetails.
    // However, looking at the user's snippet: UserEntity userDetail=(UserEntity)
    // principal;
    // So I will adapt it to cast to our User entity.

    User userDetail = (User) principal;

    Date iat = new Date();
    Date expDate = new Date(iat.getTime() + expTime);

    return Jwts.builder() // JWTs builder
        .subject(userDetail.getEmail())
        .issuedAt(iat)
        .expiration(expDate)
        .claims(Map.of("user_id", userDetail.getId(), "role", userDetail.getRole().name()))
        .signWith(key)
        .compact();
  }

  public Claims validateToken(String jwt) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(jwt)
        .getPayload();
  }
}

