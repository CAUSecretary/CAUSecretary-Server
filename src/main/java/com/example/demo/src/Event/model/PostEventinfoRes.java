package com.example.demo.src.Event.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostEventinfoRes {

    int eventIdx;
    int userIdx; //학번
    int pointId; //경도 위도가 저장되어 있음.
    int OnOff; //온라인유무
    String eventName; //이벤트이름
    String belong; //소속
    String kakaoChatUrl; //kakao openchat url
    String phone; //전화번호
    int instacralwer; // instacralwer 유무
    String period; //이벤트 기간

}
