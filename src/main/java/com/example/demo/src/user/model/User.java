package com.example.demo.src.user.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 디비에 테이블을 생성
public class User {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 전략이 데이터베이스를 따라감. -> 나중에 학번으로 받아서 교체함
    //@Column(nullable = false)
    @NotNull
    private int userIdx; //학번

    //@Column(nullable = false)
    @NotNull
    private String name; //이름
    //@Column(nullable = false)
    @NotNull
    private String phone; //전화전호
    //@Column(nullable = false)
    @NotNull
    private String email; //메일
    @NotNull
    //@Column(nullable = false)
    private String password; //비밀번호
    //@Column(nullable = false)
    @NotNull
    private String univ; //소속 대학
    //@Column(nullable = false)
    @NotNull
    private String department; //소속학부

    private String belong ; // 소속
    private String certifyImg ; //인증 사진
    private String certified ;//인증여부




}
