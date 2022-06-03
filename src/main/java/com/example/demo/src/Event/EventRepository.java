package com.example.demo.src.Event;

import com.example.demo.src.Event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository  extends JpaRepository<Event, Integer> {


//    @Query(value = "SELECT eventIdx From Event where userIdx = ?1 and instaUrl = ?2", nativeQuery = true)
//    int sellectEventIdx(int userIdx, String instaUrl);
    @Query(value = "SELECT eventIdx From Event where userIdx = ?1 and createdAt = ?2", nativeQuery = true)
    int sellectEventIdx(int userIdx, LocalDateTime createdAt);

    @Query(value = "SELECT * From Event where userIdx = ?1 and eventName = ?2",nativeQuery = true)
    Event sllectAllByuserIdxandevnetName(int userIdx, String eventname);
    @Query(value = "SELECT * From Event where userIdx = ?1 and eventIdx = ?2", nativeQuery = true)
    Event sellectByUserIdxAndEventIdx(int userIdx, int eventIdx);

    @Query(value = "SELECT contents From Event where userIdx = ?1 and eventIdx = ?2", nativeQuery = true)
    String sellectContents(int userIdx, int eventIdx);

    //event 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE From Event where userIdx = ?1 and eventIdx = ?2", nativeQuery = true)
    int deleteByUserIdxAndEventIdx(int userIdx, int eventIdx);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Event e SET e.contents = :contents Where e.userIdx = :userIdx and e.eventIdx = :eventIdx",nativeQuery = true)
    int updateContentsByUserIdxAndEventIdx(String contents, int userIdx,int eventIdx);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Event e SET e.contents = :contents and e.kakaoChatUrl :kakaoChatUrl Where e.userIdx = :userIdx and e.eventIdx = :eventIdx",nativeQuery = true)
    int updateContentsAndURLByUserIdxAndEventIdx(String contents, String kakaoChatUrl, int userIdx,int eventIdx);


    @Query(value = "SELECT userIdx, eventIdx, eventName, period,createdAt From Event where userIdx = ?1",nativeQuery = true)
    List<Object[]> showEvnetListByuserIdx(int userIdx);


//    @Query(value = "SELECT pointIdx, eventIdx, belong, OnOff, period From Event", nativeQuery = true)
//    List<Object[]> showMainPage();
    @Query(value = "SELECT E.eventIdx, E.eventName, E.belong,E.period, E.pointIdx,EP.latitude,EP.longitude,EP.location\n" +
            "FROM Event AS E\n" +
            "    INNER JOIN EventPoint AS EP on E.pointIdx = EP.pointIdx\n" +
            "WHERE OnOff = ?1", nativeQuery = true)
    List<Object[]> showMainPage(int OnOff);


    @Query(value = "SELECT eventIdx, eventName, belong, kakaoChatUrl, phone, period, contents , userIdx ,pointIdx FROM Event\n"+
            "WHERE eventIdx = ?1", nativeQuery = true)
    List<Object[]> showEachEvent(int eventIdx);






}
