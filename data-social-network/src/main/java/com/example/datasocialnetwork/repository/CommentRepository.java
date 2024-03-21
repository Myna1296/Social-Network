package com.example.datasocialnetwork.repository;

import com.example.datasocialnetwork.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.status.id = :statusId  ORDER BY c.createdDate DESC")
    Page<Comment> findCommentsByStatusId(@Param("statusId") Long statusId, Pageable pageable);

    Comment findOneById(long id);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.user.id = :userId AND c.createdDate > :startDate")
    long countCommentsByUserIdAndCreatedDateAfter(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.status.user.id = :userId AND c.createdDate > :startDate")
    long countCommentsByStatusUserIdAndCreatedDateAfter(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);
}
