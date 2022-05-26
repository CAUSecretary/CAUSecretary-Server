package com.example.demo.src.user;

import com.example.demo.src.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "SELECT userIdx, name, belong, certifyImg From User where certified = ?1", nativeQuery = true)
    List<Object[]> sellectAllUser(String certified);






}
