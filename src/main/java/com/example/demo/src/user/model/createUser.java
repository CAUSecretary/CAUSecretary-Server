package com.example.demo.src.user.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class createUser {
    private int userIdx; //학번

    private String name; //이름
    private String phone; //전화전호
    private String email; //메일
    private String password; //비밀번호
    private String univ; //소속 대학
    private String department; //소속학부

    private String belong ; // 소속
    private String certifyImg ; //인증 사진
    private String certified ;//인증여부
}
