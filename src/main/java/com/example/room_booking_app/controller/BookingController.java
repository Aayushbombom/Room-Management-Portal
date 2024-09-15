package com.example.room_booking_app.controller;

import com.example.room_booking_app.model.Booking;
import com.example.room_booking_app.repo.BookingRepo;
import com.example.room_booking_app.repo.RoomRepo;
import com.example.room_booking_app.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("api/book")
public class BookingController {
    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private UserRepo userRepo;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking, @RequestAttribute String userID){
        int userIDInt = Integer.parseInt(userID);
        booking.setDateOfBooking(setTimeToMidNight(booking.getDateOfBooking()));
        Map<String, Object> mp = new HashMap<>();
        if(!roomRepo.existsByRoomID(booking.getRoomID())){
            mp.put("Error", "Room does not exist");
            return ResponseEntity.ok(mp);
        }

        if(!userRepo.existsByUserID(userIDInt)){
            mp.put("Error", "User does not exist");
            return ResponseEntity.ok(mp);
        }

        if(bookingRepo.existsByRoomIDAndTimeOverlap(booking.getRoomID(), booking.getDateOfBooking(), booking.getTimeFrom(), booking.getTimeTo())){
            mp.put("Error", "Room unavailable");
            return ResponseEntity.ok(mp);
        }

        if(isDateBeforeToday(booking.getDateOfBooking())){
            mp.put("Error", "Invalid date/time");
            return ResponseEntity.ok(mp);
        }

        if(isDateToday(booking.getDateOfBooking()) && isTimeBeforeCurrentTime(booking.getTimeFrom())){
            mp.put("Error", "Invalid time/date/time");
            return ResponseEntity.ok(mp);
        }
        booking.setUserID(userIDInt);
        System.out.println("Booking Verified");
        bookingRepo.save(booking);
        mp.put("Success", "Booking created successfully");
        return ResponseEntity.ok(mp);

    }

    @PatchMapping
    public ResponseEntity<?> updateBooking(@RequestBody Booking booking, @RequestAttribute String userID){
        Map<String, Object> mp = new HashMap<>();
        booking.setUserID(Integer.parseInt(userID));
        if(!bookingRepo.existsByBookingID(booking.getBookingID())){
            mp.put("Error", "Booking does not exists");
            return ResponseEntity.ok(mp);
        }

        if(!roomRepo.existsByRoomID(booking.getRoomID())){
            mp.put("Error", "Room does not exist");
            return ResponseEntity.ok(mp);
        }

        if(!userRepo.existsByUserID(booking.getUserID())){
            mp.put("Error", "User does not exist");
            return ResponseEntity.ok(mp);
        }

        if(isDateBeforeToday(booking.getDateOfBooking())){
            mp.put("Error", "Invalid date/time");
            return ResponseEntity.ok(mp);
        }

        if(isDateToday(booking.getDateOfBooking()) && isTimeBeforeCurrentTime(booking.getTimeFrom())){
            mp.put("Error", "Invalid time/date/time");
            return ResponseEntity.ok(mp);
        }

        if(bookingRepo.existsByRoomIDAndTimeOverlapForUpdate(booking.getBookingID(), booking.getRoomID(), booking.getDateOfBooking(), booking.getTimeFrom(), booking.getTimeTo())){
            System.out.println("Booking Unavailable");
            mp.put("Error", "Room Unavailable");
            return ResponseEntity.ok(mp);
        }




        bookingRepo.save(booking);
        mp.put("Success", "Booking updated successfully");
        return ResponseEntity.ok(mp);

    }

    @Transactional
    @Modifying
    @DeleteMapping
    public ResponseEntity<?> deleteBooking(@RequestParam int bookingID){
        Map<String, Object> mp = new HashMap<>();
        if(!bookingRepo.existsByBookingID(bookingID)){
            mp.put("Error", "Booking does not exists");
            return ResponseEntity.ok(mp);
        }

        bookingRepo.deleteByBookingID(bookingID);
        mp.put("Success", "Booking deleted successfully");
        return ResponseEntity.ok(mp);

    }

    private static Date setTimeToMidNight(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date.setTime(cal.getTimeInMillis());

        return date;
    }

    private static boolean isTimeBeforeCurrentTime(String timeString) {
        LocalTime currentTime = LocalTime.now();
        LocalTime givenTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));

        return givenTime.isBefore(currentTime);
    }

    private static boolean isTimeAfterCurrentTime(String timeString) {
        LocalTime currentTime = LocalTime.now();
        LocalTime givenTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));

        return givenTime.isAfter(currentTime);
    }

    public static boolean isDateBeforeToday(Date date) {
        Date today = new Date(); // Get today's date
        setTimeToMidNight(today);
        // Compare the two dates
        return date.before(today);
    }

    public static boolean isDateToday(Date date) {
        Date today = new Date(); // Get today's date
        setTimeToMidNight(today);
        // Compare the two dates
        return date.equals(today);
    }
}
