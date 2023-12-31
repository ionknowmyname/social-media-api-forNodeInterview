package com.faithfulolaleru.socialmediaapi.config;

import com.faithfulolaleru.socialmediaapi.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    private final UserService userService;

    private PasswordEncoder passwordEncoder;

    private final JwtAuthFilter jwtFilter;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /* http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/v1/register/**").permitAll()
                //.requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
            )
            // .authenticationManager(authenticationManager(http, passwordEncoder, appUserService))
            .authenticationProvider(authenticationProvider())  // comment out manager & provider as needed
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        */


        http.csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/user/signup/**").permitAll()   //.hasRole("ADMIN")   // .hasAuthority("ADMIN")
            .requestMatchers("/api/v1/user/login").permitAll()  //.hasAnyRole("USER", "ADMIN")   //.hasAnyAuthority("USER", "ADMIN")("USER", "ADMIN")
            //.requestMatchers("/login/**").permitAll()  //.anonymous()
            .anyRequest().authenticated()
            .and().httpBasic()
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration
                                       authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // can also return regular AuthenticationProvider

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

}