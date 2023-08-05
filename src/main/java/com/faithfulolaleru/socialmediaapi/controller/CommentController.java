package com.faithfulolaleru.socialmediaapi.controller;

import com.faithfulolaleru.socialmediaapi.dto.CommentRequest;
import com.faithfulolaleru.socialmediaapi.dto.CommentResponse;
import com.faithfulolaleru.socialmediaapi.entity.Comment;
import com.faithfulolaleru.socialmediaapi.response.AppResponse;
import com.faithfulolaleru.socialmediaapi.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/")
    public AppResponse<?> createComment(@RequestBody CommentRequest requestDto) {

        CommentResponse response = commentService.createComment(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public AppResponse<?> getCommentById(@PathVariable("id") Long id) {

        CommentResponse response = commentService.getCommentById(id);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @GetMapping("/{postId}")
    public AppResponse<?> getCommentsByPost(@PathVariable("id") Long postId,
                                         @RequestParam(required = false, value = "page") Integer page,
                                         @RequestParam(required = false, value = "size") Integer size) {

        page = page == null || page == 0 ? 1 : page;
        size = size == null || size == 0 ? 10 : size;

        Page<Comment> response = commentService.getCommentsByPost(postId, page, size);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @PutMapping("/{id}/update")
    public AppResponse<?> updateComment(@PathVariable("id") Long id, @RequestBody CommentRequest requestDto) {

        CommentResponse response = commentService.updateComment(id, requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}/delete")
    public AppResponse<?> deleteComment(@PathVariable("id") Long id, @PathVariable("postId") Long postId) {

        String response = commentService.deleteComment(id, postId);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }
}
