package com.example.room_booking_app.repo;

import com.example.room_booking_app.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.userID = :userID AND ((b.dateOfBooking = :currentDate AND :currentTime >= b.timeTo) OR b.dateOfBooking < :currentDate)")
    List<Booking> getUserHistory(@Param("userID") int userID, @Param("currentDate") Date currentDate, @Param("currentTime") String currentTime);

    @Query("SELECT b FROM Booking b WHERE b.userID = :userID AND ((b.dateOfBooking = :currentDate AND :currentTime <= b.timeFrom) OR b.dateOfBooking > :currentDate)")
    List<Booking> getUserUpcoming(@Param("userID") int userID, @Param("currentDate") Date currentDate, @Param("currentTime") String currentTime);

    List<Booking> findByRoomID(int roomID);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.roomID = :roomId " +
            "AND b.dateOfBooking = :date " +
            "AND b.timeFrom <= :timeTo " +
            "AND b.timeTo >= :timeFrom")
    boolean existsByRoomIDAndTimeOverlap(@Param("roomId") int roomId,
                                         @Param("date") Date date,
                                         @Param("timeFrom") String timeFrom,
                                         @Param("timeTo") String timeTo);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.roomID = :roomId " +
            "AND b.dateOfBooking = :date " +
            "AND b.timeFrom <= :timeTo " +
            "AND b.timeTo >= :timeFrom " +
            "AND b.bookingID != :bookingID")
    boolean existsByRoomIDAndTimeOverlapForUpdate(@Param("bookingID") int bookingID,
                                         @Param("roomId") int roomId,
                                         @Param("date") Date date,
                                         @Param("timeFrom") String timeFrom,
                                         @Param("timeTo") String timeTo);

    boolean existsByBookingID(int bookingID);

    void deleteByBookingID(int bookingID);

    void deleteByRoomID(int roomID);
}
