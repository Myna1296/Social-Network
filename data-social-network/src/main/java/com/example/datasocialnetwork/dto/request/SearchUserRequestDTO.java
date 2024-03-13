package com.example.datasocialnetwork.dto.request;

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
