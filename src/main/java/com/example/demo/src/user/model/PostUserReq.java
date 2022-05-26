package com.example.demo.src.user.model;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Id;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostUserReq {
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 전략이 데이터베이스를 따라감. -> 나중에 학번으로 받아서 교체함
    private int userIdx; //학번

    private String name; //이름
    private String phone; //전화전호
    private String email; //메일
    private String password; //비밀번호
    private String univ; //소속 대학
    private String department; //소속학부

    private String belong ; // 소속
    private String certifyImg ; //인증 사진 --base64용
    //private MultipartFile certifyImg ; //인증 사진
    private String certified ;//인증여부

    /*
    // 회원가입 시 필요한 정보(클라에서 보내주는 정보)
    private String email;
    private String nickName;
    private int birth;
     */
}
