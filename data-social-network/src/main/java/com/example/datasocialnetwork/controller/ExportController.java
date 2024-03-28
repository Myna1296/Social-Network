package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.service.impl.ExportServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
public class ExportController {
    @Autowired
    ExportServicesImpl exportServices;

    @GetMapping("/export-file")
    public ResponseEntity<?> exportFile() {
        return exportServices.exportFile();
    }
}
