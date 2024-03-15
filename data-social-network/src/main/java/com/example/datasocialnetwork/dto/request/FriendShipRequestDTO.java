package com.example.datasocialnetwork.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FriendShipRequestDTO {
    private Long idUserSender;
    private Long idUserReceiver;
    private boolean accepte;
}
