package com.example.room_booking_app.controller;

import com.example.room_booking_app.model.Booking;
import com.example.room_booking_app.model.User;
import com.example.room_booking_app.repo.BookingRepo;
import com.example.room_booking_app.repo.RoomRepo;
import com.example.room_booking_app.repo.UserRepo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.room_booking_app.utils.JwtUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RestController
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private JwtUtils jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response){

        Map<String, Object> res = new HashMap<>();
        if(!userRepo.existsByEmail(user.getEmail())){
            res.put("Error", "User does not exists");
            return ResponseEntity.ok(res);
        }

        User actualUser = userRepo.findByEmail(user.getEmail());

        if(actualUser.getPassword().equals(user.getPassword())){
            Cookie jwt = new Cookie("jwt", jwtUtil.generateToken(String.valueOf(actualUser.getUserID())));
            jwt.setPath("/");
            jwt.setHttpOnly(true);
            jwt.setMaxAge(3600);

            response.addCookie(jwt);
            res.put("Success", "Login successful");
            return ResponseEntity.ok(res);
        }
        else{
            res.put("Error", "Wrong Password");
            return ResponseEntity.ok(res);
        }

    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user){
        Map<String, Object> res = new HashMap<>();
        if(userRepo.existsByEmail(user.getEmail())){
            res.put("Error", "Forbidden, Account already exists");
            return ResponseEntity.ok(res);
        }
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        userRepo.save(newUser);
        res.put("Success", "Account created");
        return ResponseEntity.ok(res);
    }


    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestAttribute String userID){
        int userIDInt = Integer.parseInt(userID);
        System.out.println("The userId is "+userIDInt);
        Map<String, Object> mp = new HashMap<>();
        if(!userRepo.existsByUserID(userIDInt)){
            mp.put("Error", "User does not exists");
            return ResponseEntity.ok(mp);
        }
        User user = userRepo.findByUserID(userIDInt);
        mp.put("name", user.getName());
        mp.put("userID", user.getUserID());
        mp.put("email", user.getEmail());
        System.out.println(mp);
        return ResponseEntity.ok(mp);

    }

    @GetMapping("/history")
    public ResponseEntity<?> getUserHistory(@RequestParam int userID){
        Map<String, Object> mp = new HashMap<>();
        if(!userRepo.existsByUserID(userID)){
            mp.put("Error", "User does not exists");
            return ResponseEntity.badRequest().body(mp);
        }
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String currentTime = lt.format(formatter);

        ZoneId zoneId = ZoneId.systemDefault();
        Date currentDate = Date.from(ld.atStartOfDay(zoneId).toInstant());
        List<Booking> history = bookingRepo.getUserHistory(userID, currentDate, currentTime);

        List<Map<String, Object>> response = new ArrayList<>();

        for(Booking booking : history){
            System.out.println(booking);
            Map<String, Object> bookingMap = new HashMap<>();
            bookingMap.put("userID", booking.getUserID());
            bookingMap.put("roomID", booking.getRoomID());
            bookingMap.put("bookingID", booking.getBookingID());
            bookingMap.put("dateOFBooking", booking.getDateOfBooking());
            bookingMap.put("timeFrom", booking.getTimeFrom());
            bookingMap.put("timeTo", booking.getTimeTo());

            bookingMap.put("room", roomRepo.findByRoomID(booking.getRoomID()).getRoomName());

            response.add(bookingMap);
        }

        return ResponseEntity.ok(response);

    }

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUserUpcoming(@RequestParam int userID){
        Map<String, Object> mp = new HashMap<>();
        if(!userRepo.existsByUserID(userID)){
            mp.put("Error", "User does not exists");
            return ResponseEntity.badRequest().body(mp);
        }
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String currentTime = lt.format(formatter);

        ZoneId zoneId = ZoneId.systemDefault();
        Date currentDate = Date.from(ld.atStartOfDay(zoneId).toInstant());

        List<Booking> history = bookingRepo.getUserUpcoming(userID, currentDate, currentTime);

        List<Map<String, Object>> response = new ArrayList<>();

        for(Booking booking : history){
            Map<String, Object> bookingMap = new HashMap<>();
            bookingMap.put("userID", booking.getUserID());
            bookingMap.put("roomID", booking.getRoomID());
            bookingMap.put("bookingID", booking.getBookingID());
            bookingMap.put("dateOFBooking", booking.getDateOfBooking());
            bookingMap.put("timeFrom", booking.getTimeFrom());
            bookingMap.put("timeTo", booking.getTimeTo());

            bookingMap.put("room", roomRepo.findByRoomID(booking.getRoomID()).getRoomName());

            response.add(bookingMap);
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        List<Map<String, Object>> response = new ArrayList<>();

        List<User> users = userRepo.findAll();
        for(User user : users){
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", user.getName());
            userMap.put("userID", user.getUserID());
            userMap.put("email", user.getEmail());
            response.add(userMap);

        }

        return ResponseEntity.ok(response);


    }

}
