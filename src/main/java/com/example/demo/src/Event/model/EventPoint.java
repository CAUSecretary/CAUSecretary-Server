package com.example.demo.src.Event.model;


import lombok.*;

import javax.persistence.*;




@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 디비에 테이블을 생성
public class EventPoint { //건물이 데이터로 들어올 때,refactory를 위해 table을 생성하였음.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int pointIdx;
    @Column
    private double latitude;
    @Column
    private double longitude;
//    @Column
//    private String building;

    public EventPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
