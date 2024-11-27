package com.ing.infrastructure.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(
                                        "/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                ).httpBasic(Customizer.withDefaults()) ;
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.withDefaultPasswordEncoder()
                .username("customer_1")
                .password("password")
                .authorities("ROLE_CUSTOMER", "op_customer_1")
                .build();

        UserDetails user2 = User.withDefaultPasswordEncoder()
                .username("customer_2")
                .password("password")
                .authorities("ROLE_CUSTOMER", "op_customer_2")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .authorities("ROLE_ADMIN")
                .build();

        return new InMemoryUserDetailsManager(Arrays.asList(user1, user2, admin));
    }
}