package com.example.room_booking_app.services;

import com.example.room_booking_app.model.Room;
import com.example.room_booking_app.repo.RoomRepo;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private RoomRepo roomRepo;

    public RoomService(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

//    public Room addRoom(String name, int capacity) {
//        Room exists = roomRepo.findByroomName(name);
//        if (exists != null) {
//            return null;
//        }
//        Room room = new Room(name, capacity);
//        return roomRepo.save(room);
//    }




}
