package com.cooperative.loan_management_system.security;

import com.cooperative.loan_management_system.entity.User;
import com.cooperative.loan_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Essential for H2 Console and frame-based displays
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
            )
            .userDetailsService(customUserDetailsService)
            .authorizeHttpRequests(auth -> auth
                // 2. Permit login page, static assets, and H2 database console
                .requestMatchers("/login", "/css/**", "/js/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/loans", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 4. FIX: Use null check instead of .isEmpty() to prevent compilation error
    @Bean
public CommandLineRunner initData(UserRepository repo, PasswordEncoder encoder) {
    return args -> {
        // We use .findByUsername(...).isEmpty() or == null check here
        if (repo.findByUsername("admin") == null) { 
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRole("ADMIN"); // <--- ADD THIS LINE
            repo.save(admin);
            System.out.println("----------------------------------------------");
            System.out.println("PRE-PRESENTATION SETUP: Default user created!");
            System.out.println("Username: admin | Password: admin123");
            System.out.println("----------------------------------------------");
        }
    };
}

}