package com.mymovies.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    // === CORS: origini/metodi/header da properties (mappati da ENV su Render) ===
    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins:*}") String origins,
            @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}") String methods,
            @Value("${app.cors.allowed-headers:Authorization,Content-Type,Accept}") String headers
    ) {
        CorsConfiguration cfg = new CorsConfiguration();

        // usa PATTERN per essere più tollerante (equivale agli origins passati)
        cfg.setAllowedOriginPatterns(
            Arrays.stream(origins.split(","))
                  .map(String::trim)
                  .filter(s -> !s.isEmpty())
                  .collect(Collectors.toList())
        );

        cfg.setAllowedMethods(
            Arrays.stream(methods.split(",")).map(String::trim).collect(Collectors.toList())
        );
        cfg.setAllowedHeaders(
            Arrays.stream(headers.split(",")).map(String::trim).collect(Collectors.toList())
        );

        cfg.setAllowCredentials(true);
        cfg.setMaxAge(Duration.ofHours(1));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(withDefaults())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // importantissimo: preflight libero SEMPRE
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // /error lo lasciamo aperto per evitare 403 strani su preflight deviato
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());

        return http.build();
    }
    
    /**
     * Forza il CorsFilter a stare in cima alla filter chain (prima di Spring Security).
     * Utile con proxy/CDN dove altrimenti il preflight può ricevere 403.
     */
    @Bean
    FilterRegistrationBean<CorsFilter> corsFilterRegistration(CorsConfigurationSource source) {
        FilterRegistrationBean<CorsFilter> bean =
                new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
