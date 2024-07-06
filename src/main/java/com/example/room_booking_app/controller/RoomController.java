package com.example.room_booking_app.controller;


import com.example.room_booking_app.model.Booking;
import com.example.room_booking_app.model.Room;
import com.example.room_booking_app.repo.BookingRepo;
import com.example.room_booking_app.repo.RoomRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @GetMapping
    public ResponseEntity<?> getRooms(@RequestParam(required = false) Integer capacity) {
            Map<String, String> errorResponse = new HashMap<>();
            if(capacity != null && capacity <= 0){
                errorResponse.put("Errors", "Invalid Parameters");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
            List<Room> rooms;
            if(capacity == null){
                rooms = roomRepo.findAll();
            }
            else{
                rooms = roomRepo.findByRoomCapacityGreaterThanEqual(capacity);
            }
            List<Map<String, Object>> response = new ArrayList<>();
                for (Room room : rooms) {
                    Map<String, Object> roomMap = new HashMap<>();
                    roomMap.put("roomID", room.getRoomID());
                    roomMap.put("capacity", room.getRoomCapacity());

                    List<Booking> bookings = bookingRepo.findByRoomID(room.getRoomID());
                    List<Map<String, Object>> booked = new ArrayList<>();
                    bookings.forEach(booking -> {
                        Map<String, Object> bookingMap = new HashMap<>();
                        bookingMap.put("bookingID", booking.getBookingID());
                        bookingMap.put("dateOfBooking", booking.getDateOfBooking());
                        bookingMap.put("timeFrom", booking.getTimeFrom());
                        bookingMap.put("timeTo", booking.getTimeTo());
                        bookingMap.put("purpose", booking.getPurpose());
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("userID", booking.getUserID());
                        bookingMap.put("user", userMap);
                        booked.add(bookingMap);
                    });

                    roomMap.put("booked", booked);
                    response.add(roomMap);
                }

            return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<?> addRoom(@RequestBody Room room) {
        Map<String, String> errorResponse = new HashMap<>();
        if(roomRepo.existsByRoomID(room.getRoomID())) {
            errorResponse.put("Errors", "Room already exists");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        if(room.getRoomCapacity() <= 0){
            errorResponse.put("Errors", "Invalid Parameters");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if(roomRepo.findByRoomName(room.getRoomName()) != null){
            errorResponse.put("Errors", "Room already exists");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        roomRepo.save(room);
        return new ResponseEntity<>("Room created successfully", HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> updateRoom(@RequestBody Room room) {
        Map<String, String> errorResponse = new HashMap<>();
        if(room.getRoomCapacity() <= 0){
            errorResponse.put("Errors", "Invalid Capacity");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if(!roomRepo.existsByRoomID(room.getRoomID())){
            errorResponse.put("Errors", "Room does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        roomRepo.save(room);
        return new ResponseEntity<>("Room updated successfully", HttpStatus.OK);
    }

    @Transactional
    @Modifying
    @DeleteMapping
    public ResponseEntity<?> deleteRoom(@RequestParam int roomID) {
        Map<String, String> errorResponse = new HashMap<>();
        if(!roomRepo.existsByRoomID(roomID)){
            errorResponse.put("Errors", "Room does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        bookingRepo.deleteByRoomID(roomID);
        roomRepo.deleteByRoomID(roomID);
        return new ResponseEntity<>("Room deleted successfully", HttpStatus.OK);
    }


}
