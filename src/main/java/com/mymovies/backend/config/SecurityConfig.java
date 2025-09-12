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
import org.springframework.core.Ordered;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsUtils;

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
            @Value("${app.cors.allowed-headers:*}") String headers // default più permissivo
    ) {
        CorsConfiguration cfg = new CorsConfiguration();

        // origins: SOLO schema+host(+porta). (Usiamo patterns per tolleranza)
        cfg.setAllowedOriginPatterns(
            Arrays.stream(origins.split(","))
                  .map(String::trim)
                  .filter(s -> !s.isEmpty())
                  .collect(Collectors.toList())
        );

        // consentiamo tutti gli header richiesti dal browser (minuscolo/maiuscolo)
        cfg.setAllowedHeaders(Arrays.asList("*"));

        // metodi: la tua lista va bene; se vuoi, puoi anche fare "*" per massima tolleranza
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
                                            CorsConfigurationSource corsSource) throws Exception {
        http
            .cors(c -> c.configurationSource(corsSource))   // usa il bean esplicito
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // preflight SEMPRE permesso (detector ufficiale)
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
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
