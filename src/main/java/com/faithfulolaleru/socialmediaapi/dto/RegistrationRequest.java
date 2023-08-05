package com.faithfulolaleru.socialmediaapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {

    private String username;
    private String email;
    private String base64ProfilePicture;
    private String password;

}
