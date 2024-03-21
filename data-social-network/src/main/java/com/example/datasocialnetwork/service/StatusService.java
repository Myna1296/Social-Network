package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.StatusDTO;
import com.example.datasocialnetwork.dto.request.StatusRequest;
import org.springframework.http.ResponseEntity;

public interface StatusService {
    ResponseEntity<?> getStatusByUserId(StatusRequest statusRequest);
    ResponseEntity<?> getStatusFriendUser(StatusRequest statusRequest);
    ResponseEntity<?> deleteStatus(Long id);
    ResponseEntity<?> addNewStatus(StatusDTO statusDTO);
    ResponseEntity<?> updateStatus(StatusDTO statusDTO);
    ResponseEntity<?> searchStatus(Long id);

}
