package com.example.room_booking_app.configuration;


import com.example.room_booking_app.filters.JwtFilter;
import com.example.room_booking_app.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterConfig {

    @Autowired
    private JwtUtils jwtUtil;

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration() {
        JwtFilter filter = new JwtFilter(jwtUtil);
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.setUrlPatterns(Arrays.asList("/api/user", "/api/user/upcoming","/api/book", "/api/checkjwt"));
        registrationBean.setOrder(2);// Add URL patterns for the filter to apply to// Set the order of the filter
        return registrationBean;
    }


}
