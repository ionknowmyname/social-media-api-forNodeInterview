package com.faithfulolaleru.socialmediaapi.service;

import com.faithfulolaleru.socialmediaapi.dto.PostRequest;
import com.faithfulolaleru.socialmediaapi.dto.PostResponse;
import com.faithfulolaleru.socialmediaapi.entity.Post;
import com.faithfulolaleru.socialmediaapi.entity.User;
import com.faithfulolaleru.socialmediaapi.exception.ErrorResponse;
import com.faithfulolaleru.socialmediaapi.exception.GeneralException;
import com.faithfulolaleru.socialmediaapi.repository.PostRepository;
import com.faithfulolaleru.socialmediaapi.utils.GeneralUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    private final GeneralUtils generalUtils;

    private final ModelMapper modelMapper;

    public PostResponse createPost(PostRequest requestDto) {

        User currentUser = generalUtils.getCurrentUser();

        // can duplicate post with same message, so no need to check if exists
        Post toCreate = Post.builder()
                .message(requestDto.getMessage())
                .user(currentUser)
                .createdAt(LocalDateTime.now())
                .commmentsList(new ArrayList<>())
                // .likesCount(0)
                .build();

        PostResponse response = modelMapper.map(postRepository.save(toCreate), PostResponse.class);
        response.setUserId(currentUser.getId());

        return response;
    }

    public PostResponse getPostById(Long id) {

        Post foundPost = postRepository.findById(id)
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_POST,
                        "Post with id not found"));

        return modelMapper.map(foundPost, PostResponse.class);
    }

    public Page<Post> getPostsByUser(Long userId, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return postRepository.findByUser(generalUtils.findUserById(userId), pageable);
    }

    public PostResponse updatePost(Long id, PostRequest requestDto) {

        User currentUser = generalUtils.getCurrentUser();
        Post postByUser = generalUtils.findPostByIdAndUser(id, currentUser);

        // make sure the person logged in is the creator of the post
        if(postByUser == null) {
            throw new GeneralException(HttpStatus.FORBIDDEN, ErrorResponse.ERROR_POST,
                    "Requested post not found for current user");
        }

        // you can only update message
        postByUser.setMessage(requestDto.getMessage());
        postByUser.setUpdatedAt(LocalDateTime.now());

        // you can get other postByUser details and reset them here; kinda redundant though

        PostResponse response = modelMapper.map(postRepository.save(postByUser), PostResponse.class);
        response.setUserId(currentUser.getId());

        return response;
    }

    public String deletePost(Long id) {

        User currentUser = generalUtils.getCurrentUser();
        Post postByUser = generalUtils.findPostByIdAndUser(id, currentUser);

        // make sure the person logged in is the creator of the post
        if(postByUser == null) {
            throw new GeneralException(HttpStatus.FORBIDDEN, ErrorResponse.ERROR_POST,
                    "Requested post not found for current user");
        }

        // go ahead and delete, cascade should take care of linked comments
        try {
            postRepository.delete(postByUser);
        } catch (Exception e) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_POST,
                    "Unable to delete post, try again.");
        }

        return "Post deleted successfully";
    }

}
