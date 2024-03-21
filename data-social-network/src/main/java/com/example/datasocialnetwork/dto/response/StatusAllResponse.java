package com.example.datasocialnetwork.dto.response;

import com.example.datasocialnetwork.dto.request.StatusDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class StatusAllResponse {
    private List<StatusDTO> listStatus;
    private int code;
    private String message;
    private int totalPage;
    private String userName;
    private int page;
}
