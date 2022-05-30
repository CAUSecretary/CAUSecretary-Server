package com.example.demo.src.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginRes {
    //login 성공하면 userIdx와 jwt return함.
    private int userIdx;
    private String jwt;
    private String certified;

}
