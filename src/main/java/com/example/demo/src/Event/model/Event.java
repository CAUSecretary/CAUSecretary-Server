package com.example.demo.src.Event.model;



import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 디비에 테이블을 생성
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int eventIdx; //이벤트번호
    @Column(nullable = false)
    int userIdx; //학번


    @Column
    int pointIdx; //경도 위도가 저장되어 있음.

    @Column(nullable = false)
    int OnOff; //온라인유무

    @Column(nullable = false)
    String eventName; //이벤트이름

    @Column(nullable = false)
    String belong; //소속

    @Column
    String kakaoChatUrl; //kakao openchat url
    @Column
    String phone; //전화번호

    @Column
    String instaUrl; //인스타그램 udl

    @Column(nullable = false)
    String period; //이벤트 기간

    @Column(length = 10000)
    String contents; //게시글 내용



}


/*
	event
int - onoff	온라인 오프라인유무
varcahr -eventname	이벤트 이름
char - belong	주최 (소속)
varchar - kakaochaturl	카토url
varchar - phone	연락처
varchar - instaId	인스타id
varchar - period	이벤트 기간
int - userIdx	만든사람(유저)
int - pointIdx	pointid(받아오자)
int eventIdx	eventID(PK)
	게시글 내용
 */