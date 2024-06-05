package com.gildas.springBaseProjet.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtFilter jwtFilter;
    public SecurityConfig(JwtFilter jwtFilter, BCryptPasswordEncoder passwordEncoder) {
        this.jwtFilter = jwtFilter;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers(POST,"/users/inscription").permitAll()
                                        .requestMatchers(POST,"/users/activation").permitAll()
                                        .requestMatchers(POST,"/users/connexion").permitAll()
                                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/webjars/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                                        .anyRequest().authenticated()
                )
                .sessionManagement(
                      httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                )
                .addFilterBefore(jwtFilter ,UsernamePasswordAuthenticationFilter.class)
                .build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }




}
