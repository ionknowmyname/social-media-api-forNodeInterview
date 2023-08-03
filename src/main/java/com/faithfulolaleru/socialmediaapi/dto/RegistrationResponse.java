package com.faithfulolaleru.socialmediaapi.dto;

import com.faithfulolaleru.socialmediaapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {

    private Long id;
    private String username;
    private String email;
    private String base64ProfilePicture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<User> followers = new ArrayList<>();
    private List<User> following = new ArrayList<>();
    private boolean isActive;
}
