package com.example.datasocialnetwork.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class CheckFriendShipResponse {
        private int code;
        private String message;
        private boolean checkFriendShip;

}
