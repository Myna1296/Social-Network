package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.NewStatusRequest;
import com.example.datasocialnetwork.dto.request.StatusDTO;
import com.example.datasocialnetwork.dto.request.StatusRequest;
import com.example.datasocialnetwork.dto.request.UpdateStatusRequest;
import org.springframework.http.ResponseEntity;

public interface StatusService {
    ResponseEntity<?> getNewsFeed(int pageIndex,int pageSize);
    ResponseEntity<?> getStatusByUserId(StatusRequest statusRequest);
    ResponseEntity<?> deleteStatus(Long id);
    ResponseEntity<?> addNewStatus(NewStatusRequest newStatusRequest);
    ResponseEntity<?> updateStatus(UpdateStatusRequest updateStatusRequest);
    ResponseEntity<?> searchStatus(Long id);

}
