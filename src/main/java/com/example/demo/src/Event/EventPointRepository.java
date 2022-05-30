package com.example.demo.src.Event;

import com.example.demo.src.Event.model.EventPoint;
import com.example.demo.src.Event.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventPointRepository extends JpaRepository<EventPoint,Integer> {

}
