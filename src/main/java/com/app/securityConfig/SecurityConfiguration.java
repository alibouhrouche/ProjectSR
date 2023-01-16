package com.app.securityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  private final AuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final PasswordEncoder passwordEncoder;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors().and().csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers("/api/v1/auth/**","/swagger-ui/**","/v3/api-docs/**","/api/v1/public/**")
        .permitAll()
            .requestMatchers("/api/v1/client/**").hasAnyAuthority("CLIENT")
            .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ADMIN")
            .requestMatchers("/api/v1/test","/api/v1/profile/**").hasAnyAuthority("CLIENT","ADMIN")
//        .anyRequest()
//        .authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
