package com.faithfulolaleru.socialmediaapi.dto;

import com.faithfulolaleru.socialmediaapi.entity.FollowerUser;
import com.faithfulolaleru.socialmediaapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationResponse {

    private Long id;
    private String username;
    private String email;
    private String base64ProfilePicture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FollowerUser> followers = new ArrayList<>();
    private List<FollowerUser> following = new ArrayList<>();
    private boolean isActive;
}
