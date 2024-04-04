package com.example.websocialnetwork.dto.reponse;

import com.example.websocialnetwork.dto.request.StatusInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class StatusInfoResponse {
    private StatusInfo status;
    private boolean isLike;

}
