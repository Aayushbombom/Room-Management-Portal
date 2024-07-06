package com.example.room_booking_app.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.room_booking_app.utils.JwtUtils;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    public JwtUtils jwtUtil;

    public JwtFilter(JwtUtils jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("JwtFilter");
        if(!request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
        }

            String userID = null;
            String jwt = null;
            // Check if JWT is present in the cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }
            userID = jwtUtil.extractSubject(jwt);
            if (userID != null) {
                try{

                    request.setAttribute("userID", userID);
                }
                catch(Exception e) {
                    System.out.println("userID: " + userID);
                }
            }
            chain.doFilter(request, response);
    }
}
