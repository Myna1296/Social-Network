package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.StatusDTO;
import org.springframework.http.ResponseEntity;

public interface StatusService {
    ResponseEntity<?> getStatusByUserId(Long pageId);
    ResponseEntity<?> getStatusFriendUser(Long pageId);
    ResponseEntity<?> deleteStatus(Long id);
    ResponseEntity<?> addNewStatus(StatusDTO statusDTO);
    ResponseEntity<?> updateStatus(StatusDTO statusDTO);
    ResponseEntity<?> searchStatus(Long id);

}
