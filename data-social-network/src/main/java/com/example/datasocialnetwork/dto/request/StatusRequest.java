package com.example.datasocialnetwork.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class StatusRequest {
    private Long userId  ;
    private  int pageIndex ;
    private int pageSize ;
}
