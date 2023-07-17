package com.example.youropinion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class YourOpinionApplication {

    public static void main(String[] args) {
        SpringApplication.run(YourOpinionApplication.class, args);
    }

}
