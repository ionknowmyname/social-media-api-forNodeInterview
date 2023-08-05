package com.faithfulolaleru.socialmediaapi.repository;

import com.faithfulolaleru.socialmediaapi.entity.Comment;
import com.faithfulolaleru.socialmediaapi.entity.Post;
import com.faithfulolaleru.socialmediaapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPost(Post post, Pageable pageable);

    @Query("SELECT c from comments c WHERE c.id = ?1 AND c.user = ?2 AND c.post = ?3")
    Optional<Comment> findByIdAndUserAndPost(Long id, User user, Post post);

}
