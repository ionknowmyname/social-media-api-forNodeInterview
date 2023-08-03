package com.faithfulolaleru.socialmediaapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity(name = "follower_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowerUser {

    private Long id;
    private String username;
    private String email;
    private String base64ProfilePicture;
    // private boolean isActive;    // no need
}
