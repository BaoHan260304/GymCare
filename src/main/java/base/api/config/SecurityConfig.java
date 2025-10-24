package base.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tạm thời vô hiệu hóa CSRF để dễ test, sẽ bật lại sau
            .authorizeHttpRequests(authorize -> authorize
                // Cho phép truy cập các tài nguyên tĩnh và các trang công khai
                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/login", "/register").permitAll()
                // Phân quyền dựa trên vai trò
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/trainer/**").hasRole("TRAINER")
                .requestMatchers("/customer/**").hasRole("CUSTOMER")
                // Tất cả các request khác đều cần xác thực
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // Trang đăng nhập tùy chỉnh
                .loginProcessingUrl("/perform_login") // URL xử lý đăng nhập
                .defaultSuccessUrl("/dashboard", true) // Trang sau khi đăng nhập thành công
                .failureUrl("/login?error=true") // Trang khi đăng nhập thất bại
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}