package com.example.websocialnetwork.dto.reponse;

import com.example.websocialnetwork.dto.StatusDTO;
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
