package base.api.config;

import base.api.security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public static final String[] PUBLIC_WEB_ENDPOINTS = {
            "/",
            "/login",
            "/register",
            "/css/**",
            "/js/**",
            "/images/**",
            "/error"
    };

    public static final String[] PUBLIC_API_ENDPOINTS = {
            "/auth/login",
            "/auth/register"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API, consider enabling for web forms
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_WEB_ENDPOINTS).permitAll() // Allow public web pages
                        .requestMatchers(PUBLIC_API_ENDPOINTS).permitAll() // Allow public API endpoints
                        .requestMatchers("/api/**").authenticated() // All API endpoints require authentication (JWT)
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Admin pages require ADMIN role
                        .requestMatchers("/customer/**").hasRole("CUSTOMER") // Customer pages require CUSTOMER role
                        .anyRequest().authenticated() // Any other request requires authentication
                )
                .formLogin(form -> form // Configure form-based login for web UI
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login") // URL to submit login form
                        .defaultSuccessUrl("/", true) // Redirect to home on successful login
                        .failureUrl("/login?error=true") // Redirect to login page on failure
                        .permitAll()
                )
                .logout(logout -> logout // Configure logout for web UI
                        .logoutUrl("/perform_logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Use sessions for web UI
                );

        // Add JWT filter for API endpoints, but only if the request is for an API
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
