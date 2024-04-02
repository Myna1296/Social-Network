package com.example.datasocialnetwork;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class DataSocialNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataSocialNetworkApplication.class, args);
    }

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "defjbxf34",
                "api_key", "569734145117718",
                "api_secret", "uLtGHRHAlW9QsEM39IHu9HA7aSc",
                "Secure", true));

        return cloudinary;
    }
}
