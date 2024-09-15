package com.example.room_booking_app.repo;

import com.example.room_booking_app.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepo extends JpaRepository<Room, Integer> {

    Room findByRoomID(int roomID);
    Room findByRoomName(String roomName);
    void deleteByRoomID(int roomID);

    List<Room> findByRoomCapacityGreaterThanEqual(int capacity);

    boolean existsByRoomID(int roomID);
    boolean existsByRoomName(String roomName);
}
