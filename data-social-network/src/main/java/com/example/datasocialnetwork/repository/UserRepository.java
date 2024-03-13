package com.example.datasocialnetwork.repository;


import com.example.datasocialnetwork.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Boolean existsByEmail(String email);
    Boolean existsByUserName(String username);
    User findOneByEmail(String email);
    User findOneByUserName(String userName);
    User findOneByToken(String token);
    User findOneById(Long id);
    Page<User> findByUserNameContaining(String username, Pageable pageable);
    Page<User> findAll(Pageable pageable);
}
