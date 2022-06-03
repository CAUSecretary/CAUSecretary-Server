package com.example.demo.src.Event.model;

import java.util.List;

public class PostEvent2Res {

    int eventIdx;
    int userIdx; //학번
    int pointId; //경도 위도가 저장되어 있음.
    int OnOff; //온라인유무
    String eventName; //이벤트이름
    String belong; //소속
    String kakaoChatUrl; //kakao openchat url
    String phone; //전화번호
    String period; //이벤트 기간
    String contents;
    List<String> imgs;
    int instacralwer; // instacralwer 유무

}
