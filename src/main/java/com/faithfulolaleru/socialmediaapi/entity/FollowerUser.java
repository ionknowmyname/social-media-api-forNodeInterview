package com.faithfulolaleru.socialmediaapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "follower_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowerUser {

    @Id
    @SequenceGenerator(
            name = "follower_user_sequence",
            sequenceName = "follower_user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "follower_user_sequence"
    )
    private Long id;
    private String username;
    private String email;
    private String base64ProfilePicture;
    // private boolean isActive;    // no need

    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "follower_user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<User> users;
}
