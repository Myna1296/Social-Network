package com.example.websocialnetwork.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SearchRequest {
    private String searchValue;
    private int pageIndex;
    private int pageSize;
}
