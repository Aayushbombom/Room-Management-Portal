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
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("api/rooms")
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
                    roomMap.put("name", room.getRoomName());
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
        Map<String, String> response = new HashMap<>();
        if(room.getRoomCapacity() <= 0){
            response.put("Errors", "Invalid Parameters");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if(roomRepo.findByRoomName(room.getRoomName()) != null){
            response.put("Errors", "Room already exists");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        roomRepo.save(room);

        response.put("Success", "Room added");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> updateRoom(@RequestBody Room room) {
        Map<String, String> response = new HashMap<>();
        System.out.println(room.getRoomCapacity());
        if(room.getRoomCapacity() <= 0){
            response.put("Errors", "Invalid Capacity");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if(!roomRepo.existsByRoomID(room.getRoomID())){
            response.put("Errors", "Room does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        roomRepo.save(room);
        response.put("Success", "Room updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    @Modifying
    @DeleteMapping
    public ResponseEntity<?> deleteRoom(@RequestParam int roomID) {
        Map<String, String> response = new HashMap<>();
        if(!roomRepo.existsByRoomID(roomID)){
            response.put("Errors", "Room does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        bookingRepo.deleteByRoomID(roomID);
        roomRepo.deleteByRoomID(roomID);
        response.put("Success", "Room deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
