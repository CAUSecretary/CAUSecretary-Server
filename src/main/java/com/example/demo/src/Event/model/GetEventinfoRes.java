package com.example.demo.src.Event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetEventinfoRes {
    String eventName;
    String belong;
    String kakaoChatUrl;
    String phone;
    String period;
    String contents;
    List<String> imgs;
}
