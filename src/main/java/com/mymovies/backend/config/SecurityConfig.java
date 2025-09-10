package com.mymovies.backend.config;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;
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

@Configuration
public class SecurityConfig {

	// Leggi credenziali da properties / env (con default sicuri per lo sviluppo
	// locale)
	@Value("${mmc.admin.user:user}")
	private String adminUser;

	@Value("${mmc.admin.password:1234}")
	private String adminPassword;

	@Bean
	UserDetailsService userDetailsService(PasswordEncoder encoder, AdminProperties adminProps) {
		return new InMemoryUserDetailsManager(User.withUsername(adminProps.getUser())
				.password(encoder.encode(adminProps.getPassword())).roles("ADMIN").build());
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// CORS per Angular
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration cfg = new CorsConfiguration();
		cfg.setAllowedOrigins(List.of("http://localhost:4200"));
		cfg.setAllowedMethods(List.of("GET", "POST", "DELETE", "OPTIONS"));
		cfg.setAllowedHeaders(List.of("Content-Type", "Authorization"));
		cfg.setAllowCredentials(false);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cfg);
		return source;
	}

	// Metodo preciso: definizione del SecurityFilterChain per API stateless + CORS
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(withDefaults()) // usa CORS da WebConfig
				.csrf(csrf -> csrf.disable()) // API stateless → niente CSRF
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// smoke test: tutto aperto. (Rendere più restrittivo dopo i test)
						.anyRequest().permitAll());

		return http.build();
	}

}
