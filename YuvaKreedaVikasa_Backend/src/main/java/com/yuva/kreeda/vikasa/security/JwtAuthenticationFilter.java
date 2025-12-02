package com.yuva.kreeda.vikasa.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String jwt = authHeader.substring(7);
      try {
        Claims claims = jwtUtils.validateToken(jwt);
        String email = claims.getSubject();

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = userDetailsService.loadUserByUsername(email);

          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception e) {
        System.out.println("Invalid JWT Token: " + e.getMessage());
      }
    }

    filterChain.doFilter(request, response);
  }
}