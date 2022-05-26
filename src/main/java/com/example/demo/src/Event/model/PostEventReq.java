package com.example.demo.src.Event.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostEventReq {

    int userIdx; //학번
    int pointIdx; //경도 위도가 저장되어 있음.
    int OnOff; //온라인유무


    String eventName; //이벤트이름
    String belong; //소속
    String kakaoChatUrl; //kakao openchat url
    String phone; //전화번호

    int instacralwer; // instacralwer 유무

    String instaUrl; //인스타그램 udl
    String period; //이벤트 기간
    String contents; //게시글 내용


}
