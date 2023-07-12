package com.paymybuddy.backend;

import com.paymybuddy.backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements ApplicationRunner {

    private static JwtUtils jwtUtils;

    @Value("${jwt.secret}")
    private String secret;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        jwtUtils = new JwtUtils(secret);
    }

    public static JwtUtils getJwtUtils() {
        return jwtUtils;
    }
}
