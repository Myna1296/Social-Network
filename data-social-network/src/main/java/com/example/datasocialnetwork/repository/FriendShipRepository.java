package com.example.datasocialnetwork.repository;

import com.example.datasocialnetwork.entity.FriendShip;
import com.example.datasocialnetwork.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip,Long> {

    @Query("SELECT f FROM FriendShip f WHERE (f.userSender.id = :userId OR f.userReceiver.id = :userId) AND f.accepted = true ORDER BY f.createdDate DESC")
    Page<FriendShip> findAcceptedFriendshipsByUserIdWithLimitOffset(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("SELECT COUNT(f) FROM FriendShip f WHERE (f.userSender.id = :userId OR f.userReceiver.id = :userId) AND f.accepted = true")
    long countAcceptedFriendshipsByUserId(@Param("userId") Long userId);

//    @Query("SELECT f FROM FriendShip f WHERE f.userSender.id = :userId AND f.accepted = false ORDER BY f.createdDate DESC")
//    List<FriendShip> findUnacceptedFriendshipsByUserSenderWithLimitOffset(
//            @Param("userId") Long userId,
//            @Param("limit") int limit,
//            @Param("offset") int offset
//    );
//
//    @Query("SELECT COUNT(f) FROM FriendShip f WHERE f.userSender.id = :userId AND f.accepted = false")
//    long countUnacceptedFriendshipsByUserSender(@Param("userId") Long userId);
//
//    @Query("SELECT f FROM FriendShip f WHERE f.userReceiver.id = :userId AND f.accepted = false ORDER BY f.createdDate DESC")
//    List<FriendShip> findUnacceptedFriendshipsByUserReceiverWithLimitOffset(@Param("userId") Long userId,@Param("limit") int limit,
//                                                                            @Param("offset") int offset);
//
//    @Query("SELECT COUNT(f) FROM FriendShip f WHERE f.userReceiver.id = :userId AND f.accepted = false")
//    long countUnacceptedFriendshipsByUserReceiver(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FriendShip f WHERE (f.userSender = :userSender AND f.userReceiver = :userReceiver) OR (f.userSender = :userReceiver AND f.userReceiver = :userSender)")
    boolean checkFriendshipExists(@Param("userSender") User userSender, @Param("userReceiver") User userReceiver);

}
