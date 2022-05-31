package com.example.demo.src.Event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class GetMainPageRes {

    private int eventIdx;
    private String eventName;
    private String belong;
    private String period;
    private int pointIdx;
    private double latitude;
    private double longitude;
    private String location;

}
