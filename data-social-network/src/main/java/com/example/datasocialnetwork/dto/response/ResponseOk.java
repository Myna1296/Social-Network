package com.example.datasocialnetwork.dto.response;

import lombok.Data;

@Data
public class ResponseOk {
    private int code;
    private String message;
    // Default constructor
    public ResponseOk() {
    }

    // Constructor with parameters
    public ResponseOk(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
