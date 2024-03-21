package com.example.datasocialnetwork.repository;

import com.example.datasocialnetwork.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface StatusRepository extends JpaRepository<Status,Long > {

    @Query("SELECT s FROM Status s WHERE s.user.id = :userId ORDER BY s.createdDate DESC")
    Page<Status> findStatusByUserIdWithLimitOffset(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT s FROM Status s " +
            "JOIN FriendShip f ON (CASE " +
            "WHEN f.userSender.id = :userId THEN f.userReceiver.id " +
            "WHEN f.userReceiver.id = :userId THEN f.userSender.id " +
            "END) = s.user.id " +
            "JOIN User u ON (u.id = :userId) " +
            "WHERE (f.userReceiver.id = :userId OR f.userSender.id = :userId) " +
            "AND f.accepted = true " +
            "ORDER BY s.createdDate DESC")
    Page<Status> findStatusOfAllFriends(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT s FROM Status s WHERE s.id = :id")
    Status findStatusById(@Param("id") Long id);

    @Query("SELECT COUNT(s) FROM Status s WHERE s.id = :id and s.createdDate > :startDate")
    long countStatusTAndCreatedDateAfter(@Param("id") Long id, @Param("startDate") LocalDateTime startDate);
}