package com.faithfulolaleru.socialmediaapi.service;


import com.faithfulolaleru.socialmediaapi.dto.RegistrationRequest;
import com.faithfulolaleru.socialmediaapi.dto.RegistrationResponse;
import com.faithfulolaleru.socialmediaapi.entity.User;
import com.faithfulolaleru.socialmediaapi.exception.ErrorResponse;
import com.faithfulolaleru.socialmediaapi.exception.GeneralException;
import com.faithfulolaleru.socialmediaapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.BAD_REQUEST,
                        ErrorResponse.ERROR_USER,
                        "Invalid User Credentials"));
    }

    public User findUserByEmail(String email) {

        return userRepository.findByEmail(email)
            .orElseThrow(() -> new GeneralException(
                HttpStatus.BAD_REQUEST,
                ErrorResponse.ERROR_USER,
                "Invalid User Credentials"
            ));
    }

    public Map<String, Object> signUpAppUser(User entity) {

        boolean userExist = userRepository.existsByEmail(entity.getEmail());
        if(userExist) {
            // TODO check if attributes are the same and
            // TODO if email not confirmed send confirmation email.
            throw new GeneralException(HttpStatus.CONFLICT, ErrorResponse.ERROR_APP_USER,
                    "AppUser with email already exists");
        }

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        AppUserEntity savedAppUser = appUserRepository.save(entity);


        // Create OTP for AppUser

        String otp = UUID.randomUUID().toString();
        String otp2 = Utils.generateOtp();

        OtpEntity otpEntity = OtpEntity.builder()
                .otp(otp)
                .appUser(savedAppUser)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();

        boolean isSaved = otpService.save(otpEntity);

        if(!isSaved) {
            throw new GeneralException(HttpStatus.CONFLICT, ErrorResponse.ERROR_OTP,
                    "OTP was not saved Successfully");
        }

        //TODO : send email

        Map<String, Object> response = new HashMap<>();
        response.put("otp", otp);
        response.put("userId", savedAppUser.getId());
        response.put("savedAppUser", savedAppUser);

        return response;
    }

    public RegistrationResponse registerUser(RegistrationRequest requestDto) {

        boolean userExist = userRepository.existsByEmail(requestDto.getEmail());
        if(userExist) {
            // TODO check if attributes are the same
            throw new GeneralException(HttpStatus.CONFLICT, ErrorResponse.ERROR_USER,
                    "User with email already exists");
        }

        User toSave = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .base64ProfilePicture(requestDto.getBase64ProfilePicture())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .followers(new ArrayList<>())
                .following(new ArrayList<>())
                .build();

        return modelMapper.map(userRepository.save(toSave), RegistrationResponse.class);
    }

    public int activateUser(String email) {
        return userRepository.enableAppUser(email);
    }

    public User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_USER, "User with id not found"));
    }

    private Collection<? extends SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        return authorities;
    }


}
