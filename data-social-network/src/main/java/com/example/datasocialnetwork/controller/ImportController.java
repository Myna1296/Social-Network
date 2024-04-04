package com.example.datasocialnetwork.controller;

import com.cloudinary.utils.ObjectUtils;
import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.service.impl.ExportServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/import-file")
public class ImportController {
    @Autowired
    ExportServicesImpl exportServices;

    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importFile( @RequestParam(value = "file" , required = false) MultipartFile file) {
        if( file == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is not ");
        }
        
        return exportServices.exportFile();
    }


}
