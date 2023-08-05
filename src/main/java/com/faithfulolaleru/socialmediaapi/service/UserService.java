package com.faithfulolaleru.socialmediaapi.service;


import com.faithfulolaleru.socialmediaapi.config.jwt.JwtService;
import com.faithfulolaleru.socialmediaapi.dto.*;
import com.faithfulolaleru.socialmediaapi.entity.FollowerUser;
import com.faithfulolaleru.socialmediaapi.entity.User;
import com.faithfulolaleru.socialmediaapi.exception.ErrorResponse;
import com.faithfulolaleru.socialmediaapi.exception.GeneralException;
import com.faithfulolaleru.socialmediaapi.repository.UserRepository;
import com.faithfulolaleru.socialmediaapi.utils.GeneralUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final GeneralUtils generalUtils;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.BAD_REQUEST,
                        ErrorResponse.ERROR_USER,
                        "Invalid User Credentials"));
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
                .createdAt(LocalDateTime.now())
                .build();

        return modelMapper.map(userRepository.save(toSave), RegistrationResponse.class);
    }

    public RegistrationResponse updateUser(Long id, UpdateUserRequest requestDto) {

        User currentUser = generalUtils.getCurrentUser();
        User toUpdate = generalUtils.findUserById(id);

        // make sure the person logged in is the owner of the account
        if(!currentUser.getId().equals(toUpdate.getId())) {
            throw new GeneralException(HttpStatus.FORBIDDEN, ErrorResponse.ERROR_USER,
                    "Cannot update user");
        }

        if(requestDto.getFollowId() != null && requestDto.getAction() != null) {

            List<FollowerUser> currentFollowers = toUpdate.getFollowing();
            boolean alreadyFollowing;

            switch(requestDto.getAction()) {
                case "ADD":
                    // check to make sure user to add exists, then check that its not already in the follower list

                    User toAdd = generalUtils.findUserById(requestDto.getFollowId());

                    alreadyFollowing = currentFollowers.stream()
                            .map(followerUser -> followerUser.getEmail()).
                            anyMatch(email -> email.equals(toAdd.getEmail()));

                    if(alreadyFollowing) {
                        throw new GeneralException(HttpStatus.FORBIDDEN, ErrorResponse.ERROR_USER,
                                "You already follow user");
                    }

                    // else add user to your follower list
                    currentFollowers.add(generalUtils.convertUserToFollowerUser(toAdd));
                    toUpdate.setFollowing(currentFollowers);

                    break;
                case "REMOVE":
                    // check to make sure the user to remove exists in follower list

                    User toRemove = generalUtils.findUserById(requestDto.getFollowId());

                    alreadyFollowing = currentFollowers.stream()
                            .map(followerUser -> followerUser.getEmail()).
                            anyMatch(email -> email.equals(toRemove.getEmail()));

                    if(!alreadyFollowing) {
                        throw new GeneralException(HttpStatus.FORBIDDEN, ErrorResponse.ERROR_USER,
                                "You don't follow user");
                    }

                    // go ahead and remove user from following list
                    currentFollowers.remove(generalUtils.findFollowerUserByEmail(toRemove.getEmail()));
                    toUpdate.setFollowing(currentFollowers);

                    break;
                default:
                    throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_REQUIRED,
                            "Invalid action");
            }

        }

        // if there's a value to update then update, else keep existing value
        toUpdate.setUsername((requestDto.getUsername() != null) ? requestDto.getUsername() : toUpdate.getUsername());
        toUpdate.setEmail((requestDto.getEmail() != null) ? requestDto.getEmail() : toUpdate.getEmail());
        toUpdate.setBase64ProfilePicture((requestDto.getBase64ProfilePicture() != null)
                ? requestDto.getBase64ProfilePicture() : toUpdate.getBase64ProfilePicture());
        toUpdate.setPassword((requestDto.getPassword() != null) ? passwordEncoder.encode(requestDto.getPassword())
                : toUpdate.getPassword());
        toUpdate.isActive(); // didn't want to refactor to Boolean
        toUpdate.setUpdatedAt(LocalDateTime.now());

        // should be redundant but just put in case, so it doesn't override existing with new nothing
        toUpdate.setFollowers(toUpdate.getFollowers());
        toUpdate.setFollowing(toUpdate.getFollowing());
        toUpdate.setCreatedAt(toUpdate.getCreatedAt());

        return modelMapper.map(userRepository.save(toUpdate), RegistrationResponse.class);
    }

    public String deleteUser(Long id) {

        User currentUser = generalUtils.getCurrentUser();
        User toDelete = generalUtils.findUserById(id);

        // make sure the person logged in is the owner of the account
        if(!currentUser.getId().equals(toDelete.getId())) {
            throw new GeneralException(HttpStatus.FORBIDDEN, ErrorResponse.ERROR_USER,
                    "Cannot delete user");
        }

        try {
            userRepository.delete(toDelete);
        } catch (Exception e) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_USER,
                    "Unable to delete user, try again.");
        }

        return "User deleted successfully";
    }

    public int activateUser(String email) {
        return userRepository.enableAppUser(email);
    }
}
