package com.example.demo.src.Event;

import com.example.demo.src.Event.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo,Integer> {

    @Query(value = "select imgurl from Photo where userIdx = ?1 and eventIdx = ?2",nativeQuery = true)
    List<String> sellectAllImgurl(int userIdx, int eventIdx);


    @Transactional
    @Modifying
    @Query(value = "DELETE From Event where userIdx = ?1 and eventIdx = ?2", nativeQuery = true)
    boolean deleteByUserIdxAndEventIdx(int userIdx, int eventIdx);




}
