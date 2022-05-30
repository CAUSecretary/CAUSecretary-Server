package com.example.demo.src.Event.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class GetEventListRes {
    private int userIdx; //이벤트 발급한 유저 학번
    private int eventIdx; //이벤트 고유 아이디
    private String eventName; //이벤트 이름
    private String period; //이벤트 진행 기간
    private String createdAt; //이벤트 생성 시간

}
