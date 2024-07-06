package com.example.room_booking_app;

import com.example.room_booking_app.filters.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class RoomBookingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomBookingAppApplication.class, args);
    }


}
