package com.example.datasocialnetwork.common;

public enum SendCodeType {
    RECOVERY("OTP ForgotPassword"),
    LOGIN("OTP Login");
    private String name;

    SendCodeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
