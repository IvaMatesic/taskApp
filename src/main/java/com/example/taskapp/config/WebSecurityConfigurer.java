package com.example.taskapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfigurer {

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("adminsecret"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("regularUser")
                .password(passwordEncoder.encode("usersecret"))
                .roles("USER")
                .build();


        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable().authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/private/**"))
                        .authenticated())
                .authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/public/**"))
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
