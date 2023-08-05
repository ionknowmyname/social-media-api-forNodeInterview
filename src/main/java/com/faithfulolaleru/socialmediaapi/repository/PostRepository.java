package com.faithfulolaleru.socialmediaapi.repository;

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

@Repository
@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser(User user, Pageable pageable);


    Optional<Post> findByIdAndUser(Long id, User user);



}
