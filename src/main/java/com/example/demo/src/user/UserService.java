package com.example.demo.src.user;


import com.example.demo.config.BaseException;

import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.src.user.model.createUser;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.util.Base64;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }


    //회원가입
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // 이메일 중복 확인
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        //회원가입
        try{
            System.out.println("userservice 회원가입 초입");
            String filename = "";
            String check_certify = "N";//인증할 필요가 없다.
            //소속있음
            if(!(postUserReq.getBelong().equals(null) || postUserReq.getBelong().equals(""))){
                System.out.println("소속있음");
                check_certify = "F";//인증이 필요한데, 아직 미인증 상태.
                //MultipartFile image = postUserReq.getCertifyImg();
                String image = postUserReq.getCertifyImg();
                if(image == null ||image.isEmpty()) {
                    System.out.println("소속있는데 사진이 비어있음.");
                    throw new BaseException(POST_USERS_EMPTY_CERTIFY_IMG);
                }
                //확장자
                //String prefix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".")+1, image.getOriginalFilename().length());
                String prefix = "png";
                filename = postUserReq.getEmail()+"."+prefix;
                System.out.println("filename: "+filename);
                String filepath = "/Users/chaehuiseon/chs_documents/caucap/befor_certification/";

                //파일 없으면 만들자!
                File folder = new File(filepath);
                if(!folder.isDirectory()) {
                    folder.mkdirs();
                }

                //인증 이미지 저장
                File dest = new File(filepath+filename);
                FileOutputStream fileOutputStream = new FileOutputStream(dest);
                try {
                    //String carg = URLDecoder.decode(image, "UTF-8");
                    //carg = carg.replaceAll("\n","");

                    byte[] imageBase64Decode = Base64.getDecoder().decode(image);

                    //image.transferTo(dest);
                    fileOutputStream.write(imageBase64Decode);
                    fileOutputStream.close();
                    System.out.println("저장 완료");
                }catch(Exception e) {
                    System.out.println("저장 실패");
                    e.printStackTrace();
                }

            }
            createUser User = new createUser(postUserReq.getUserIdx(),
                    postUserReq.getName(), postUserReq.getPhone(),postUserReq.getEmail(),
                    postUserReq.getPassword(),postUserReq.getUniv(),postUserReq.getDepartment(),
                    postUserReq.getBelong(),filename,check_certify);

            System.out.println("자 드가자 : "+User.toString());


            int userIdx = userDao.createUser(User);
            //int userIdx = userDao.createUser(postUserReq);
            System.out.println("User Service userIdx (db들어간후임): " + userIdx);
            if (userIdx == 0){
                throw new Exception();
            }else{
                //jwt 발급.
                // TODO: jwt는 다음주차에서 배울 내용입니다!

                String jwt = jwtService.createJwt(userIdx);
                return new PostUserRes(jwt,userIdx);

            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
