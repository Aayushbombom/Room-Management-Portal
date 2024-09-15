package com.example.room_booking_app.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.room_booking_app.utils.JwtUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtFilter extends OncePerRequestFilter {

    public JwtUtils jwtUtil;

    public JwtFilter(JwtUtils jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Access-Control-Allow-Origin");
            response.setHeader("Access-Control-Allow-Credentials", "true");


            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
            String userID = null;
            String jwt = null;
            String cookieName = null;
            // Check if JWT is present in the cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName()) || "Adminjwt".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        cookieName = cookie.getName();
                        break;
                    }
                }
            }
            userID = jwtUtil.extractSubject(jwt);
            if (userID != null) {
                if(request.getRequestURI().endsWith("/checkjwt")){
                    response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
                    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, DELETE, OPTIONS");
                    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Access-Control-Allow-Origin");
                    response.setHeader("Access-Control-Allow-Credentials", "true");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setHeader("type", cookieName);
                    String jsonResponse = "{"
                            + "\"type\":\"" + cookieName + "\""
                            + "}";

                    response.getWriter().write(jsonResponse);

                    return;
                }
                try {
                    request.setAttribute("userID", userID);
                } catch (Exception e) {
                    System.out.println("userID: " + userID);
                }
            }
            chain.doFilter(request, response);

    }
}
