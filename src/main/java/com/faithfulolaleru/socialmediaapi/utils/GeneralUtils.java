package com.faithfulolaleru.socialmediaapi.utils;

import com.faithfulolaleru.socialmediaapi.dto.PostResponse;
import com.faithfulolaleru.socialmediaapi.entity.Comment;
import com.faithfulolaleru.socialmediaapi.entity.FollowerUser;
import com.faithfulolaleru.socialmediaapi.entity.Post;
import com.faithfulolaleru.socialmediaapi.entity.User;
import com.faithfulolaleru.socialmediaapi.exception.ErrorResponse;
import com.faithfulolaleru.socialmediaapi.exception.GeneralException;
import com.faithfulolaleru.socialmediaapi.repository.CommentRepository;
import com.faithfulolaleru.socialmediaapi.repository.PostRepository;
import com.faithfulolaleru.socialmediaapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class GeneralUtils {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return findUserByEmail(email);
    }

    public User findUserByEmail(@NotNull String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_USER,
                        "User with email not found"));

    }

    public FollowerUser findFollowerUserByEmail(@NotNull String email) {

        return userRepository.findByEmail(email).map(user -> convertUserToFollowerUser(user))
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_USER,
                        "User with email not found"));

    }

    public User findUserById(@NotNull Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_USER,
                        "User with id not found"));

    }

    public Post findPostById(@NotNull Long id) {

        return postRepository.findById(id)
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_POST,
                        "Post with id not found"));

    }

    public Comment findCommentById(Long id) {

        return commentRepository.findById(id)
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_COMMENT,
                        "Comment with id not found"));
    }

    public FollowerUser convertUserToFollowerUser(User user) {

        return FollowerUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .base64ProfilePicture(user.getBase64ProfilePicture())
                .build();
    }

    public Post findPostByIdAndUser(Long id, User user) {

        return postRepository.findByIdAndUser(id, user).orElse(null);
    }

    public Comment findCommentByIdAndUserAndPost(Long id, User user, Post post) {

        return commentRepository.findByIdAndUserAndPost(id, user, post).orElse(null);
    }

    public List<Comment> findCommentsByPostId(Long postId) {

        Post foundPost = findPostById(postId);

        return foundPost.getCommmentsList();
    }

    public boolean isLoggedInUser(User loggedInUser, User testUser) {

        // if() {}

        return false;
    }


}
