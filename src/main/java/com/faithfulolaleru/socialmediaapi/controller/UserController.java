package com.faithfulolaleru.socialmediaapi.controller;


import com.faithfulolaleru.socialmediaapi.dto.*;
import com.faithfulolaleru.socialmediaapi.exception.ErrorResponse;
import com.faithfulolaleru.socialmediaapi.exception.GeneralException;
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


    @PostMapping("/signup")
    public AppResponse<?> registerUser(@RequestBody RegistrationRequest requestDto) {

        RegistrationResponse response = userService.registerUser(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    @PutMapping("/{id}/update")
    public AppResponse<?> updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserRequest requestDto) {

        if(id == null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_REQUIRED,
                    "User id is required");
        }

        RegistrationResponse response = userService.updateUser(id, requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}/delete")
    public AppResponse<?> deleteUser(@PathVariable("id") Long id) {

        String response = userService.deleteUser(id);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @PostMapping("/login")
    public AppResponse<?> loginUser(@RequestBody LoginRequest requestDto) {

        LoginResponse response = userService.loginUser(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }


}
