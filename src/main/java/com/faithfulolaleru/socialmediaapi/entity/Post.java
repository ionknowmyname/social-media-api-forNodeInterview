package com.faithfulolaleru.socialmediaapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;


@Entity(name = "posts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @SequenceGenerator(
            name = "post_sequence",
            sequenceName = "post_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String message;

//    @Column(name = "likes_count")
//    private int likesCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JdbcTypeCode(SqlTypes.JSON)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Comment> commmentsList;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
