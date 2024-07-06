package com.example.room_booking_app.model;

import jakarta.persistence.*;

@Entity
@Table(name="Rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomID;

    private String roomName;
    private int roomCapacity;

    // Constructors

    public Room() {
        // Default constructor
    }

    public Room(String name, int capacity) {
        this.roomName = name;
        this.roomCapacity = capacity;
    }

    // Getters
    public int getRoomID() {return roomID;}
    public int getRoomCapacity() {return roomCapacity;}
    public String getRoomName() {return roomName;}

    // Setters
    public void setRoomCapacity(int roomCapacity) {this.roomCapacity = roomCapacity;}
    public void setRoomName(String roomName) {this.roomName = roomName;}

    // toString() method for debugging
    @Override
    public String toString() {
        return "Room{" +
                ", roomID=" + roomID +
                ", roomCapacity=" + roomCapacity +
                '}';
    }
}

