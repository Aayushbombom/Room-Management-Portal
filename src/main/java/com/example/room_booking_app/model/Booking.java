package com.example.room_booking_app.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="Bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingID;

    private int userID;

    private int roomID;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBooking;

    private String timeFrom;
    private String timeTo;
    private String purpose;

    // Constructors, getters, and setters

    public Booking() {
        // Default constructor
    }

    public Booking(int userID, int roomID, Date dateOfBooking, String timeFrom, String timeTo, String purpose) {
        this.userID = userID;
        this.roomID = roomID;
        this.dateOfBooking = dateOfBooking;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.purpose = purpose;
    }

    // Getters
    public int getBookingID() { return bookingID; }
    public int getUserID() {return userID;}
    public int getRoomID() {return roomID;}
    public Date getDateOfBooking() {return dateOfBooking;}
    public String getTimeFrom() {return timeFrom;}
    public String getTimeTo() {return timeTo;}
    public String getPurpose() {return purpose;}

    // Setters
    public void setUserID(int userID) {this.userID = userID;}
    public void setRoomID(int roomID) {this.roomID = roomID;}
    public void setDateOfBooking(Date dateOfBooking) {this.dateOfBooking = dateOfBooking;}
    public void setTimeFrom(String timeFrom) {this.timeFrom = timeFrom;}
    public void setTimeTo(String timeTo) {this.timeTo = timeTo;}
    public void setPurpose(String purpose) {this.purpose = purpose;}

    // toString() method for debugging
    @Override
    public String toString() {
        return "Booking{" +
                ", userID=" + userID +
                ", roomID=" + roomID +
                ", dateOfBooking=" + dateOfBooking +
                ", timeFrom='" + timeFrom + '\'' +
                ", timeTo='" + timeTo + '\'' +
                ", purpose='" + purpose + '\'' +
                '}';
    }
}

