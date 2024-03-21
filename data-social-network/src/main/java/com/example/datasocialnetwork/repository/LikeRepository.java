package com.example.datasocialnetwork.repository;

import com.example.datasocialnetwork.entity.LikeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LikeRepository extends JpaRepository<LikeStatus, Long> {

    @Query("SELECT COUNT(l) FROM LikeStatus l WHERE l.user.id = :userId AND l.createdDate >= :startDate")
    long countLikesByUserIdAndCreatedDateAfter(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(l) FROM LikeStatus l WHERE l.status.user.id = :userId AND l.createdDate >= :startDate")
    long countLikesByStatusUserIdAndCreatedDateAfter(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(l) FROM LikeStatus l WHERE l.status.id = :statusId AND l.user.id = :userId")
    long countByStatusIdAndUserId(@Param("statusId") Long statusId, @Param("userId") Long userId);

    @Query("SELECT l FROM LikeStatus l WHERE l.status.id = :statusId AND l.status.user.id = :userId")
    LikeStatus findLikesByStatusIdAndUserId(@Param("statusId") Long statusId, @Param("userId") Long userId);
}