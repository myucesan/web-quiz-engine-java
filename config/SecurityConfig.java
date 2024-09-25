package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())     // Default Basic auth config
                .csrf(configurer -> configurer.disable()) // for POST requests via Postman
                .authorizeHttpRequests((requests) ->  requests
                        .antMatchers(HttpMethod.POST, "/api/register").permitAll()
                        .antMatchers("/api/quizzes").authenticated()
                        .antMatchers(HttpMethod.GET, "/api/quizzes/{id}").authenticated()
                        .antMatchers(HttpMethod.GET, "/api/quizzes?page={page}").authenticated()
                        .antMatchers(HttpMethod.GET, "/api/quizzes/completed?page={number}").authenticated()
                        .antMatchers(HttpMethod.DELETE, "/api/quizzes/{id}").authenticated()
                        .antMatchers(HttpMethod.POST, "/api/quizzes/{id}/solve").authenticated()
                        .antMatchers("/actuator/shutdown").permitAll()
                        .antMatchers("/error").permitAll()
                        .anyRequest().denyAll()
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

