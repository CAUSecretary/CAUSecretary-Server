package com.example.demo.src.Event.model;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 디비에 테이블을 생성
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int photoIdx;
    @Column(nullable = false)
    int eventIdx;
    @Column(nullable = false)
    int userIdx;
    @Column(length = 10000, nullable = false)
    String imgurl;

    public Photo(int eventIdx, int userIdx, String imgurl){
        this.eventIdx = eventIdx;
        this.userIdx = userIdx;
        this.imgurl = imgurl;

    }

}
