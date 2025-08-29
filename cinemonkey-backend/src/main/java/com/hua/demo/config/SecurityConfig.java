package com.hua.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint = new CustomAuthenticationEntryPoint();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf().disable()
                .cors(c -> {
                    CorsConfigurationSource cs = r -> {
                        CorsConfiguration cc = new CorsConfiguration();
                        cc.setAllowedOriginPatterns(List.of("*"));
                        cc.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                        cc.setAllowedHeaders(List.of("*"));
                        cc.setAllowCredentials(true);
                        return cc;
                    };
                    c.configurationSource(cs);
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/posters/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/api/users/{username}",
                                "/api/users/{userEmail}/reservations",
                                "/api/movies",
                                "/api/movies/{title}",
                                "/api/movies/{title}/showings",
                                "/api/reservations/save/{username}-{id}",
                                "/api/reservations/requestCancelation/{id}"
                        ).hasAnyRole("USER", "EMPLOYEE", "EMPLOYER", "ADMIN")
                        .requestMatchers(
                                "/api/movies",
                                "/api/users",
                                "/api/reservations",
                                "/api/showings",
                                "/api/auth/register"
                        ).hasAnyRole("EMPLOYEE", "EMPLOYER", "ADMIN")
                        .requestMatchers("/**").hasAnyRole("EMPLOYER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(token -> token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter()))
                                .authenticationEntryPoint(authenticationEntryPoint)
                );

        return httpSecurity.build();
    }
}
