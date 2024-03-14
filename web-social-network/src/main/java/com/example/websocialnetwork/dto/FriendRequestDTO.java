package com.example.websocialnetwork.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FriendRequestDTO {
    private Long id;
    private int page;
    // Constructor with parameters
    public FriendRequestDTO(Long id, int page) {
        this.id = id;
        this.page = page;
    }
}
