package com.example.websocialnetwork.dto.reponse;

import com.example.websocialnetwork.dto.StatusDTO;
import com.example.websocialnetwork.dto.request.StatusInfo;
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
