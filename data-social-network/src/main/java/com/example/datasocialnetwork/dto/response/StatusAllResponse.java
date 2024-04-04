package com.example.datasocialnetwork.dto.response;

import com.example.datasocialnetwork.dto.request.StatusInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class StatusAllResponse {
    private List<StatusInfo> listStatus;
    private int totalPage;
}
