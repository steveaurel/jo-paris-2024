package com.infoevent.authservice.config;

import com.infoevent.authservice.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the authentication service.
 * Defines beans for user details service, password encoder, authentication provider,
 * authentication manager, and security filter chain configuration.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class AuthConfig {

    /**
     * Defines a custom UserDetailsService to load user-specific data.
     *
     * @return the custom UserDetailsService implementation
     */
    @Bean
    public UserDetailsService userDetailsService() {
        log.info("Configuring custom UserDetailsService.");
        return new CustomUserDetailsService();
    }

    /**
     * Configures the security filter chain to specify custom security settings.
     *
     * @param http HttpSecurity to configure
     * @return SecurityFilterChain for processing HTTP requests
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring HttpSecurity.");

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/auth/sign-up", "/auth/sign-in", "/auth/validate").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable); // Disables CSRF protection for simplicity

        return http.build();
    }

    /**
     * Defines the password encoder to be used by the authentication provider.
     *
     * @return the BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Configuring BCryptPasswordEncoder.");
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the DaoAuthenticationProvider with custom UserDetailsService and password encoder.
     *
     * @return the configured AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        log.info("Configuring DaoAuthenticationProvider.");
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Exposes the default AuthenticationManager as a Bean.
     *
     * @param config AuthenticationConfiguration for obtaining the AuthenticationManager
     * @return the AuthenticationManager to be used across the application
     * @throws Exception if an error occurs obtaining the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("Configuring AuthenticationManager.");
        return config.getAuthenticationManager();
    }
}
