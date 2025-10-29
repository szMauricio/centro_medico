package com.porfolio.centro_medico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**").permitAll()
                        // Pacientes
                        .requestMatchers(HttpMethod.GET, "/pacientes").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/pacientes/dni/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/pacientes").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/pacientes/**").hasAuthority("ADMIN")
                        // Médicos - endpoints públicos para consulta
                        .requestMatchers(HttpMethod.GET, "/medicos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/medicos/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/medicos/especialidad/**").permitAll()
                        // Médicos - endpoints protegidos para administración
                        .requestMatchers(HttpMethod.POST, "/medicos").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/medicos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/medicos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/medicos/**").hasAuthority("ADMIN")
                        // Turnos
                        .requestMatchers(HttpMethod.GET, "/turnos").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/turnos/rango").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/turnos").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/turnos/**/cancelar")
                        .hasAnyAuthority("USER", "ADMIN", "MEDICO")
                        .requestMatchers(HttpMethod.PATCH, "/turnos/**/completar").hasAnyAuthority("MEDICO", "ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
