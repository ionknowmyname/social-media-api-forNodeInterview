package com.faithfulolaleru.socialmediaapi.controller;


import com.faithfulolaleru.socialmediaapi.dto.RegistrationRequest;
import com.faithfulolaleru.socialmediaapi.dto.RegistrationResponse;
import com.faithfulolaleru.socialmediaapi.response.AppResponse;
import com.faithfulolaleru.socialmediaapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final static String responseMessage = "App user token is %s";


    @PostMapping("/")
    public AppResponse<?> registerUser(@RequestBody RegistrationRequest requestDto) {

        RegistrationResponse response = userService.registerUser(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .data(response)
                .build();
    }


}
