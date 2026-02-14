package com.codingshuttle.youtube.hospitalManagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf-> csrf.disable())
                .sessionManagement(sessionConfig-> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->{
                    auth
                            .requestMatchers("/public/**", "/auth/**").permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(httpBasic-> httpBasic.disable())
                .formLogin(formLogin->formLogin.disable());
        return httpSecurity.build();
    }
}
