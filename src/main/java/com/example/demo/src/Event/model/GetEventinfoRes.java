package com.example.demo.src.Event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetEventinfoRes {
    @Column(nullable = true)
    String eventName;
    @Column(nullable = true)
    String belong;
    @Column(nullable = true)
    String kakaoChatUrl;
    @Column(nullable = true)
    String phone;
    @Column(nullable = true)
    String period;
    @Column(nullable = true)
    String contents;
    @Column(nullable = true)
    List<String> imgs;
    String location;
    int pointIdx;
}
