package com.faithfulolaleru.socialmediaapi.dto;

import com.faithfulolaleru.socialmediaapi.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private Long userId;  // return userId instead of whole User
    private Long postId;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
