package com.example.demo.src.auth.model;



import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User { //유저의 정보를 담아두는 객체

    @NotNull
    private int userIdx; //학번
    @NotNull
    private String name; //이름
    @NotNull
    private String phone; //전화전호
    @NotNull
    private String email; //메일
    @NotNull
    private String password; //비밀번호
    @NotNull
    private String univ; //소속 대학
    @NotNull
    private String department; //소속학부

    private String belong ; // 소속
    private String certifyImg ; //인증 사진
    private String certified ;//인증여부




}
