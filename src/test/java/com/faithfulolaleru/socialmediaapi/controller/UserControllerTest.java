package com.faithfulolaleru.socialmediaapi.controller;

import com.faithfulolaleru.socialmediaapi.dto.RegistrationRequest;
import com.faithfulolaleru.socialmediaapi.dto.RegistrationResponse;
import com.faithfulolaleru.socialmediaapi.service.LoginService;
import com.faithfulolaleru.socialmediaapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
// @ExtendWith(ParameterResolver.class)
// @ExtendWith({ RestDocumentationExtension.class, SpringExtension.class})
@ContextConfiguration(classes = { UserController.class })
// @TestPropertySource(locations = "/localTest.properties")   // for test DB
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private LoginService loginService;

    private final ObjectMapper objectMapper = new ObjectMapper();



    @Test
    void registerUser() throws Exception {

        RegistrationRequest registrationRequest = getRegistrationRequest();
        RegistrationResponse registrationResponse = getRegistrationResponse();

        when(userService.registerUser(any(RegistrationRequest.class))).thenReturn(registrationResponse);

        mockMvc.perform(post("/api/v1/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk()).andDo(print())
               .andExpect(jsonPath("$.data.id").value(Long.valueOf(1)))
               .andExpect(jsonPath("$.data.username").value("testUsername"))
               .andExpect(jsonPath("$.data.email").value("test@email.com"))
               .andExpect(jsonPath("$.data.base64ProfilePicture").value("testbase64ImageString"))
                .andExpect(jsonPath("$.data.isActive").value(false));
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    private RegistrationRequest getRegistrationRequest() {
        return RegistrationRequest.builder()
                .username("testUsername")
                .email("test3@email.com")
                .base64ProfilePicture("testbase64ImageString")
                .password("11111")
                .build();
    }

    private RegistrationResponse getRegistrationResponse() {
        return RegistrationResponse.builder()
                .id(Long.valueOf(1))
                .username("testUsername")
                .email("test3@email.com")
                .base64ProfilePicture("testbase64ImageString")
                .followers(new ArrayList<>())
                .following(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .isActive(false)
                .build();
    }
}