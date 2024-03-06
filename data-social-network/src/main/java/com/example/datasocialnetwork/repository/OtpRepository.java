package com.example.datasocialnetwork.repository;

import com.example.datasocialnetwork.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp,Long> {
    Otp findOneByEmail(String email);
    void deleteByEmail(String email);
}
