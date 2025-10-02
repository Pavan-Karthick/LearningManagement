package com.lms.project.config;

import com.lms.project.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Security filter chain bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF as we are using stateless authentication
                .csrf(csrf -> csrf.disable())
                // Set session management to stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // No sessions will be created
                )
                // Configure endpoint security
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers("/signup", "/login").permitAll()

                                // student endpoints
                                .requestMatchers("/enrollments/**").hasRole("STUDENT")
                                .requestMatchers(HttpMethod.GET, "/courses/**").hasAnyRole("STUDENT","INSTRUCTOR","ADMIN")

                                // instructor/admin course management
                                .requestMatchers(HttpMethod.POST, "/courses").hasAnyRole("INSTRUCTOR","ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/courses/**").hasAnyRole("INSTRUCTOR","ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/courses/**").hasAnyRole("INSTRUCTOR","ADMIN")

                                // admin endpoints
                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                // everything else authenticated
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Additional configuration can go here (e.g., JWT filters)

        return http.build();
    }
}
