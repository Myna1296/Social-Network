package com.example.datasocialnetwork.dto.response;

import com.example.datasocialnetwork.dto.request.StatusInfo;
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
