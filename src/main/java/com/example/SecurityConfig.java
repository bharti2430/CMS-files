package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private StudentService studentService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/", "/admin/admin_login", "/student/login", "/student/register", "/css/**", "/images/**").permitAll()
                .requestMatchers("/student/student", "/complaint").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/student/login")
                .loginProcessingUrl("/student/login")
                .usernameParameter("email")  // Specify the username parameter
                .passwordParameter("password")  // Specify the password parameter
                .permitAll()
                .defaultSuccessUrl("/student/student", true)
                .failureUrl("/student/login?error=true")
                .successHandler((request, response, authentication) -> {
                    logger.info("Login successful for user: {}", authentication.getName());
                    response.sendRedirect("/student/student");
                })
                .failureHandler((request, response, exception) -> {
                    logger.error("Login failed: {}", exception.getMessage());
                    response.sendRedirect("/student/login?error=true");
                })
            )
            .logout(logout -> logout
                .permitAll()
                .logoutSuccessUrl("/student/login?logout=true")
            ).csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return studentService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(studentService).passwordEncoder(passwordEncoder());
    }
}