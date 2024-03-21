package com.example.websocialnetwork.dto.reponse;

import com.example.websocialnetwork.dto.StatusDTO;
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
