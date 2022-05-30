package com.example.demo.src.auth.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostAuthloginRes {
    //login 성공하면 userIdx와 jwt return함.
    private int userIdx;
    private String jwt;
    private List<JSONObject> uncertified;



}
