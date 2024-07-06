package com.example.room_booking_app.repo;

import com.example.room_booking_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);
    User findByEmail(String email);

    boolean existsByUserID(int id);
    User findByUserID(int id);

}
