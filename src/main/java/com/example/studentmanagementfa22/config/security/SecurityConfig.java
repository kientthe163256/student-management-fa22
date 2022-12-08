package com.example.studentmanagementfa22.config.security;

import com.example.studentmanagementfa22.config.security.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(auth -> auth
                        .antMatchers("/student/**").hasRole("STUDENT")
                        .antMatchers("/teacher/**").hasRole("TEACHER")
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/register/**", "/", "/login").permitAll()
                        .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                )
                .csrf().disable()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler))
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .successHandler(customSuccessHandler)
//                        .permitAll()
//                )
                .logout()
                    .logoutSuccessHandler(logoutSuccessHandler())
                    .permitAll()
                ;
        return http.build();
    }
}
