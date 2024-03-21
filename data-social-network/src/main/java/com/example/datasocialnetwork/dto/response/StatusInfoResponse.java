package com.example.datasocialnetwork.dto.response;

import com.example.datasocialnetwork.dto.request.StatusDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class StatusInfoResponse {
    private StatusDTO status;
    private boolean isLike;
    private int code;
    private String message;

}
