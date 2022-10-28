package com.example.studentmanagementfa22.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.SessionManagementFilter;

@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(auth -> auth       //lambda no need .and()
                        .antMatchers("/student/**").hasRole("STUDENT")
                        .antMatchers("/teacher/**").hasRole("TEACHER")
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/register/**", "/").permitAll()
                )
                .csrf().disable()
                .addFilterBefore(corsFilter(), SessionManagementFilter.class)
                .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler))
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(customSuccessHandler)
                        .permitAll()
                        )
                .logout()
                    .logoutSuccessHandler(logoutSuccessHandler())
                    .permitAll()
;
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter(){
        return new CorsFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
