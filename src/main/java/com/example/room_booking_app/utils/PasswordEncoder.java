package com.example.room_booking_app.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class PasswordEncoder {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
