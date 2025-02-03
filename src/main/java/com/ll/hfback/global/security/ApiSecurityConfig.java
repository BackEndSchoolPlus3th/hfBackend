package com.ll.hfback.global.security;

import com.ll.hfback.global.app.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        // 위에서 설정한 CORS 설정 코드와 동일
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin(AppConfig.getSiteFrontUrl());
        corsConfig.addAllowedOrigin(AppConfig.getDevFrontUrl());
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", corsConfig);

        return source;
    }

    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers(HttpMethod.GET, "/api/*/members").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/*/members/*").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/members/*/verify-password").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/auth/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/*/auth/logout").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                                .anyRequest().permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/auth/logout").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/auth/signup").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/auth/email/verification-code").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/auth/email/verify").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/auth/find-account").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/auth/password/reset").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/auth/password/reset/verify").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/api/*/auth/password/reset/new").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/session").permitAll()
                                .requestMatchers("/oauth2/**").permitAll()
                                .requestMatchers("/login/oauth2/**").permitAll()
                                .anyRequest().authenticated()
                )
                .headers(headers ->
                    headers.frameOptions(frameOptions ->
                        frameOptions.sameOrigin()
                    )
                )
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(
                    AbstractHttpConfigurer::disable
                )
                .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                    )
                )
                .oauth2Login(
                    oauth2Login -> oauth2Login
                        .successHandler(customOAuth2AuthenticationSuccessHandler)
                        .authorizationEndpoint(
                            authorizationEndpoint -> authorizationEndpoint
                                .authorizationRequestResolver(customAuthorizationRequestResolver)
                        )
                )
                .addFilterBefore(
                    customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                );

        // CORS 설정 적용
        http.addFilterBefore(new CorsFilter(corsConfigurationSource()), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
