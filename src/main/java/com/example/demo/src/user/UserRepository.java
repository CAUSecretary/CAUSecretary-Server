package com.example.demo.src.user;

import com.example.demo.src.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "SELECT userIdx, name, belong, certifyImg From User where certified = ?1", nativeQuery = true)
    List<Object[]> sellectAllUser(String certified);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE User u SET u.certified = :certified Where u.userIdx = :userIdx and u.belong = :belong",nativeQuery = true)
    int updatecertified (String certified, int userIdx,String belong);


    @Query(value = "SELECT certified From User where userIdx = ?1 and belong = ?2", nativeQuery = true )
    String checkcertified(int userIDx, String belong);






}
