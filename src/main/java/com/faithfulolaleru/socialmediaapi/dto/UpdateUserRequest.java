package com.faithfulolaleru.socialmediaapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {

    private String username;
    private String email;
    private String base64ProfilePicture;
    private String password;
    private boolean isActive;
    private Long followId;      // id of user you want to follow/unfollow
    private String action;   // what do you want to do with the followId   // switch this to an enum later
}
