package com.faithfulolaleru.socialmediaapi.service;

import com.faithfulolaleru.socialmediaapi.dto.RegistrationRequest;
import com.faithfulolaleru.socialmediaapi.dto.RegistrationResponse;
import com.faithfulolaleru.socialmediaapi.dto.UpdateUserRequest;
import com.faithfulolaleru.socialmediaapi.entity.FollowerUser;
import com.faithfulolaleru.socialmediaapi.entity.User;
import com.faithfulolaleru.socialmediaapi.exception.GeneralException;
import com.faithfulolaleru.socialmediaapi.repository.UserRepository;
import com.faithfulolaleru.socialmediaapi.utils.GeneralUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(classes = { UserService.class, ModelMapper.class })
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private GeneralUtils generalUtils;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;


    private ModelMapper modelMapper = new ModelMapper();


    @Test
    void registerUser_Success() {

        User user = getUserEntity(Long.valueOf(1), "test@email.com");
        RegistrationRequest registrationRequest = getRegistrationRequest();

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        RegistrationResponse fromTest = modelMapper.map(user, RegistrationResponse.class);
        RegistrationResponse fromService = userService.registerUser(registrationRequest);

        assertThat(fromService).isNotNull();
        assertThat(fromService.getEmail()).isEqualTo(fromTest.getEmail());
    }

    @Test
    void registerUser_UserExists() {

        RegistrationRequest registrationRequest = getRegistrationRequest();

        when(userRepository.existsByEmail(any())).thenReturn(true);

        Exception exception = assertThrows(GeneralException.class, () -> userService.registerUser(registrationRequest));

        assertEquals("User with email already exists", exception.getMessage());
    }

    @Test
    void updateUser_AddFollowing() {

        User user1 = getUserEntity(Long.valueOf(1), "test@email.com");
        User user2 = getUserEntity(Long.valueOf(2), "test2@email.com");
        User user3 = getUpdatedUserEntity();
        UpdateUserRequest updateUserRequest = getUpdateUserRequest();

        when(generalUtils.getCurrentUser()).thenReturn(user1);
        when(generalUtils.findUserById(Long.valueOf(1))).thenReturn(user1);

        when(generalUtils.findUserById(updateUserRequest.getFollowId())).thenReturn(user2);

        when(generalUtils.convertUserToFollowerUser(user2)).thenReturn(getFollowerUser());
        when(userRepository.save(any())).thenReturn(user3);

        RegistrationResponse fromTest = modelMapper.map(user3, RegistrationResponse.class);
        RegistrationResponse fromService = userService.updateUser(Long.valueOf(1) , updateUserRequest);

        assertThat(fromService).isNotNull();
        assertThat(fromService.getFollowing()).isEqualTo(fromTest.getFollowing());
    }

    @Test
    void deleteUser() {

        User user = getUserEntity(Long.valueOf(1), "test@email.com");

        when(generalUtils.getCurrentUser()).thenReturn(user);
        when(generalUtils.findUserById(anyLong())).thenReturn(user);

        // when(userRepository.delete(any(User.class))).thenReturn(null);

        String fromService = userService.deleteUser(user.getId());

        assertEquals("User deleted successfully", fromService);
    }

    private RegistrationRequest getRegistrationRequest() {
        return RegistrationRequest.builder()
                .username("testUsername")
                .email("test@email.com")
                .base64ProfilePicture("testbase64ImageString")
                .password("11111")
                .build();
    }

    private UpdateUserRequest getUpdateUserRequest() {
        return UpdateUserRequest.builder()
                .username("testUsername")
                .email("test@email.com")
                .base64ProfilePicture("testbase64ImageString")
                .isActive(false)
                .followId(Long.valueOf(2))
                .action("ADD")
                .build();
    }

    private User getUserEntity(Long id, String email) {
        return User.builder()
                .id(id)
                .username("testUsername")
                .email(email)
                .base64ProfilePicture("testbase64ImageString")
                .followers(new ArrayList<>())
                .following(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .isActive(false)
                .build();
    }

    private User getUpdatedUserEntity() {
        FollowerUser followerUser = getFollowerUser();
        List<FollowerUser> followingList = List.of(followerUser);

        return User.builder()
                .id(Long.valueOf(1))
                .username("testUsername")
                .email("test@email.com")
                .base64ProfilePicture("testbase64ImageString")
                .followers(new ArrayList<>())
                .following(followingList)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(false)
                .build();
    }

    public FollowerUser getFollowerUser() {
        return FollowerUser.builder()
                .id(Long.valueOf(2))
                .username("testUsername")
                .email("test2@email.com")
                .base64ProfilePicture("testbase64ImageString")
                .build();
    }


}