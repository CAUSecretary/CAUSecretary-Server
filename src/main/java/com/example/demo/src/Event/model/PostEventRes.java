package com.example.demo.src.Event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostEventRes {

    int eventIdx;
    int userIdx;
    int pointIdx;
    int OnOff;
    String eventName;
    String belong;
    String kakaoChatUrl;
    String phone;
    String period;
    String contents;
    List<String> imgs;
    int instacralwer; // instacralwer 유무



}
