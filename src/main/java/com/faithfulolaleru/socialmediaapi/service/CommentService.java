package com.faithfulolaleru.socialmediaapi.service;

import com.faithfulolaleru.socialmediaapi.dto.CommentRequest;
import com.faithfulolaleru.socialmediaapi.dto.CommentResponse;
import com.faithfulolaleru.socialmediaapi.entity.Comment;
import com.faithfulolaleru.socialmediaapi.entity.Post;
import com.faithfulolaleru.socialmediaapi.entity.User;
import com.faithfulolaleru.socialmediaapi.exception.ErrorResponse;
import com.faithfulolaleru.socialmediaapi.exception.GeneralException;
import com.faithfulolaleru.socialmediaapi.repository.CommentRepository;
import com.faithfulolaleru.socialmediaapi.utils.GeneralUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    private final GeneralUtils generalUtils;

    private final ModelMapper modelMapper;

    public CommentResponse createComment(CommentRequest requestDto) {

        User currentUser = generalUtils.getCurrentUser();

        // check that post exists
        Post foundPost = generalUtils.findPostById(requestDto.getPostId());

        // can duplicate comments with same message on a post, so no need to check if exists
        Comment toCreate = Comment.builder()
                .message(requestDto.getMessage())
                .user(currentUser)
                .post(foundPost)
                .createdAt(LocalDateTime.now())
                .build();

        CommentResponse response = modelMapper.map(commentRepository.save(toCreate), CommentResponse.class);
        response.setUserId(currentUser.getId());
        response.setPostId(foundPost.getId());

        return response;
    }

    public CommentResponse getCommentById(Long id) {

        Comment foundComment = commentRepository.findById(id)
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_COMMENT,
                        "Comment with id not found"));

        return modelMapper.map(foundComment, CommentResponse.class);
    }

    public Page<Comment> getCommentsByPost(Long postId, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Comment> commentList = generalUtils.findCommentsByPostId(postId);

        return new PageImpl<>(commentList, pageable, commentList.size());
    }

    public CommentResponse updateComment(Long id, CommentRequest requestDto) {

        User currentUser = generalUtils.getCurrentUser();
        Comment foundComment = generalUtils.findCommentById(id);

        // make sure the person logged in is the owner of the comment
        if(!currentUser.getId().equals(foundComment.getUser().getId())) {
            throw new GeneralException(HttpStatus.FORBIDDEN, ErrorResponse.ERROR_COMMENT,
                    "Cannot update comment");
        }

        // make sure comment belongs to post
        if(!requestDto.getPostId().equals(foundComment.getPost().getId())) {
            throw new GeneralException(HttpStatus.FORBIDDEN, ErrorResponse.ERROR_COMMENT,
                    "Comment does not belong to post");
        }

        foundComment.setMessage(requestDto.getMessage());

        CommentResponse response = modelMapper.map(commentRepository.save(foundComment), CommentResponse.class);
        response.setUserId(currentUser.getId());
        response.setPostId(foundComment.getPost().getId());

        return response;
    }

    public String deleteComment(Long id, Long postId) {

        User currentUser = generalUtils.getCurrentUser();
        Post foundPost = generalUtils.findPostById(postId);
        Comment foundComment = generalUtils.findCommentByIdAndUserAndPost(id, currentUser, foundPost);

        // make sure comment is by current user & is for post you want to delete for
        if(foundComment == null) {
            throw new GeneralException(HttpStatus.FORBIDDEN, ErrorResponse.ERROR_COMMENT,
                    "Comment not found for post & user");
        }

        try {
            commentRepository.delete(foundComment);
        } catch (Exception e) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_COMMENT,
                    "Unable to delete comment, try again.");
        }

        return "Comment deleted successfully";
    }
}
