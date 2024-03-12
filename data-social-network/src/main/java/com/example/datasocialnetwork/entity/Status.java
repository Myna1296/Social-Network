package com.example.datasocialnetwork.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "status")
    private List<LikeStatus> likeStatusList;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "status_image")
    private String statusImage;

    @Column(name = "status_text", nullable = false, columnDefinition = "TEXT")
    private String statusText;

    @Column(name = "createdate", nullable = false)
    private LocalDateTime createdDate;

    @Transient
    private int likeCount;

    public int getLikeCount() {
        return likeStatusList != null ? likeStatusList.size() : 0;
    }
}
