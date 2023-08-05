package com.faithfulolaleru.socialmediaapi.service;

import com.faithfulolaleru.socialmediaapi.config.jwt.JwtService;
import com.faithfulolaleru.socialmediaapi.dto.LoginRequest;
import com.faithfulolaleru.socialmediaapi.dto.LoginResponse;
import com.faithfulolaleru.socialmediaapi.entity.User;
import com.faithfulolaleru.socialmediaapi.exception.ErrorResponse;
import com.faithfulolaleru.socialmediaapi.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public record LoginService(JwtService jwtService, AuthenticationManager authenticationManager) {

    public LoginResponse loginUser(LoginRequest requestDto) {

        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();

            throw new GeneralException(
                    HttpStatus.BAD_REQUEST,
                    ErrorResponse.ERROR_USER,
                    "Invalid User Credentials"
            );
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generateToken(authentication);
    }

    private Collection<? extends SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        return authorities;
    }
}
