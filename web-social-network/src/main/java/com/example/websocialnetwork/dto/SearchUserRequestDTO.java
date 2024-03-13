package com.example.websocialnetwork.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SearchUserRequestDTO {
    private String search;
    private int page;
}
