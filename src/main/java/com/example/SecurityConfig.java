package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdminService adminService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain studentSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/student/**", "/complaint/**")
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/student/login", "/student/register", "/css/**", "/images/**").permitAll()
                .requestMatchers("/student/student", "/complaint").hasRole("STUDENT")
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/student/login")
                .loginProcessingUrl("/student/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
                .defaultSuccessUrl("/student/student", true)
                .failureUrl("/student/login?error=true")
            )
            .logout(logout -> logout
                .permitAll()
                .logoutUrl("/student/logout")
                .logoutSuccessUrl("/student/login?logout=true")
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/admin/**")
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/admin/admin_login", "/admin/admin_register", "/css/**", "/images/**").permitAll()
                .requestMatchers("/admin/admin_dashboard").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/admin/admin_login")
                .loginProcessingUrl("/admin/admin_login")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
                .defaultSuccessUrl("/admin/admin_dashboard", true)
                .failureUrl("/admin/admin_login?error=true")
                .successHandler((request, response, authentication) -> {
                    logger.info("Admin login successful: {}", authentication.getName());
                    response.sendRedirect("/admin/admin_dashboard");
                })
                .failureHandler((request, response, exception) -> {
                    logger.error("Admin login failed: {}", exception.getMessage());
                    response.sendRedirect("/admin/admin_login?error=true");
                })
            )
            .logout(logout -> logout
                .permitAll()
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/admin_login?logout=true")
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(studentService).passwordEncoder(passwordEncoder());
        auth.userDetailsService(adminService).passwordEncoder(passwordEncoder());
    }
}