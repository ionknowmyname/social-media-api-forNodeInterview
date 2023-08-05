package com.faithfulolaleru.socialmediaapi.controller;

import com.faithfulolaleru.socialmediaapi.dto.PostRequest;
import com.faithfulolaleru.socialmediaapi.dto.PostResponse;
import com.faithfulolaleru.socialmediaapi.dto.RegistrationRequest;
import com.faithfulolaleru.socialmediaapi.dto.RegistrationResponse;
import com.faithfulolaleru.socialmediaapi.entity.Post;
import com.faithfulolaleru.socialmediaapi.response.AppResponse;
import com.faithfulolaleru.socialmediaapi.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping("/")
    public AppResponse<?> createPost(@RequestBody PostRequest requestDto) {

        PostResponse response = postService.createPost(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public AppResponse<?> getPostById(@PathVariable("id") Long id) {

        PostResponse response = postService.getPostById(id);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @GetMapping("/{userId}")
    public AppResponse<?> getPostsByUser(@PathVariable("id") Long userId,
                                         @RequestParam(required = false, value = "page") Integer page,
                                         @RequestParam(required = false, value = "size") Integer size) {

        page = page == null || page == 0 ? 1 : page;
        size = size == null || size == 0 ? 10 : size;

        Page<Post> response = postService.getPostsByUser(userId, page, size);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @PutMapping("/{id}/update")
    public AppResponse<?> updatePost(@PathVariable("id") Long id, @RequestBody PostRequest requestDto) {

        PostResponse response = postService.updatePost(id, requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}/delete")
    public AppResponse<?> deletePost(@PathVariable("id") Long id) {

        String response = postService.deletePost(id);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }
}
