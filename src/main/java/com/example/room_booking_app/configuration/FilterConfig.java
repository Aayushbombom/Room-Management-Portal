package com.example.room_booking_app.configuration;

import com.example.room_booking_app.filters.CorsFilter;
import com.example.room_booking_app.filters.JwtFilter;
import com.example.room_booking_app.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private JwtUtils jwtUtil;

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CorsFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); // Ensure this filter is executed first
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtRequestFilterRegistration() {
        JwtFilter filter = new JwtFilter(jwtUtil);
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/user");
        registrationBean.setOrder(2);// Add URL patterns for the filter to apply to// Set the order of the filter
        return registrationBean;
    }


}
