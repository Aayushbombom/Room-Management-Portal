package com.example.room_booking_app;

import com.example.room_booking_app.filters.JwtFilter;
import com.example.room_booking_app.utils.PasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class RoomBookingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomBookingAppApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder();
    }

}
