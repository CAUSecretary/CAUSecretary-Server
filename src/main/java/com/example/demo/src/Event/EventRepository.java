package com.example.demo.src.Event;

import com.example.demo.src.Event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface EventRepository  extends JpaRepository<Event, Integer> {


    @Query(value = "SELECT eventIdx From Event where userIdx = ?1 and instaUrl = ?2", nativeQuery = true)
    int sellectEventIdx(int userIdx, String instaUrl);

    @Query(value = "SELECT * From Event where userIdx = ?1 and eventIdx = ?2", nativeQuery = true)
    Event sellectByUserIdxAndEventIdx(int userIdx, int eventIdx);

    @Query(value = "SELECT contents From Event where userIdx = ?1 and eventIdx = ?2", nativeQuery = true)
    String sellectContents(int userIdx, int eventIdx);

    //event 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE From Event where userIdx = ?1 and eventIdx = ?2", nativeQuery = true)
    boolean deleteByUserIdxAndEventIdx(int userIdx, int eventIdx);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Event e SET e.contents = :contents Where e.userIdx = :userIdx and e.eventIdx = :eventIdx",nativeQuery = true)
    int updateContentsByUserIdxAndEventIdx(String contents, int userIdx,int eventIdx);
}
