package com.example.datasocialnetwork.entity;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "like_status")
public class LikeStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Column(name = "createdate", nullable = false)
    private LocalDateTime createdDate;

}
