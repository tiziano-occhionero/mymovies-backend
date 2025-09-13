package com.mymovies.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {

    // === Credenziali admin da ENV/props (con default di sviluppo) ===
    @Value("${mmc.admin.user:user}")
    private String adminUser;

    @Value("${mmc.admin.password:1234}")
    private String adminPassword;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
            User.withUsername(adminUser)
                .password(encoder.encode(adminPassword))
                .roles("ADMIN")
                .build()
        );
    }

    // === CORS configurabile via properties/ENV ===
    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins:*}") String origins,
            @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}") String methods,
            @Value("${app.cors.allowed-headers:*}") String headers
    ) {
        CorsConfiguration cfg = new CorsConfiguration();

        cfg.setAllowedOriginPatterns(
            Arrays.stream(origins.split(","))
                  .map(String::trim)
                  .filter(s -> !s.isEmpty())
                  .collect(Collectors.toList())
        );

        // consenti tutti gli header richiesti dal browser
        cfg.setAllowedHeaders(Arrays.asList("*"));

        cfg.setAllowedMethods(
            Arrays.stream(methods.split(","))
                  .map(String::trim)
                  .collect(Collectors.toList())
        );

        cfg.setAllowCredentials(true);
        cfg.setMaxAge(Duration.ofHours(1));
        cfg.setExposedHeaders(Arrays.asList("Location","Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            @Qualifier("corsConfigurationSource") CorsConfigurationSource corsSource) throws Exception {
        http
            .cors(c -> c.configurationSource(corsSource))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(org.springframework.web.cors.CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET,  "/api/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.HEAD, "/api/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
        return http.build();
    }



    /** Metti il CorsFilter in testa alla chain (utile dietro CDN/proxy). */
    @Bean
    FilterRegistrationBean<CorsFilter> corsFilterRegistration(
            @Qualifier("corsConfigurationSource") CorsConfigurationSource source) {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
