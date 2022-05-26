package com.example.demo.src.auth;

import com.example.demo.config.BaseException;
import com.example.demo.src.auth.model.PostAuthloginRes;
import com.example.demo.src.auth.model.PostLoginReq;
import com.example.demo.src.auth.model.PostLoginRes;
import com.example.demo.src.auth.model.User;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserRepository;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
//import java.util.Base64;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;



@Service
public class AuthService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthDao authDao;
    private final AuthProvider authProvider;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @Autowired
    public AuthService(AuthDao authDao, AuthProvider authProvider, JwtService jwtService, UserRepository userRepository) {
        this.authDao = authDao;
        this.authProvider = authProvider;
        this.jwtService = jwtService;
        this.userRepository = userRepository;

    }

    //로그인 : user
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        //클라이언트에서 받은 객체에서 email을 받아서, 이 email을 가진 해당 유저를 리턴을 해줌.
        // (DB에서 회원가입이 되어 있는지를 확인하면서 가지고 오는거임)
        User user = authDao.getPwd(postLoginReq);
        String encryptPwd;

        try{
            //클라이언트에서 입력한 비밀번호를 암호함.
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());

        } catch (Exception exception) {

            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);

        }

        //DB에 저장되어 있는 비밀번호와 일치히는지를 확인함.
        if(user.getPassword().equals(encryptPwd)){
            //일치하면 해당 userIdx 를 받아옴
            int userIdx = user.getUserIdx();
            //userIdx를 이용해서 jwt 토큰 생성
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx, jwt); //토큰발급
        } else {
            throw new BaseException(FAILED_TO_LOGIN);
        }
        //int userIdx = user.getUserIdx();
        //String jwt = jwtService.createJwt(userIdx);

        //return new PostLoginRes(userIdx, jwt);

    }

    //관리자 로그인
    public PostAuthloginRes authlogin(PostLoginReq postLoginReq) throws BaseException {
        //클라이언트에서 받은 객체에서 email을 받아서, 이 email을 가진 해당 유저를 리턴을 해줌.
        // (DB에서 회원가입이 되어 있는지를 확인하면서 가지고 오는거임)
        User user = authDao.getPwd(postLoginReq);
        String encryptPwd;

        try{
            //클라이언트에서 입력한 비밀번호를 암호함.
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());

        } catch (Exception exception) {

            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);

        }

        //DB에 저장되어 있는 비밀번호와 일치히는지를 확인함.
        if(user.getPassword().equals(encryptPwd)){
            //일치하면 해당 userIdx 를 받아옴.
            int userIdx = user.getUserIdx();
            String filepath = "/Users/chaehuiseon/chs_documents/caucap/befor_certification/";

            JSONObject jsonObject = new JSONObject();
            JSONArray userArray = new JSONArray();
            if ( userIdx == 1){ //관리자다.. --> 관리자가 맞다.

                //미인증된 유저들을 모두가져와라
                List<Object[]> results = userRepository.sellectAllUser("F");

                for(Object[] result : results){
                    String base64Image = "";
                    String filename = result[3].toString();
                    System.out.println("filename : "+filename);
                    File file = new File(filepath+filename);
                    try {
                        FileInputStream imageInFile = new FileInputStream(file);
                        // Reading a Image file from file system
                        byte imageData[] = new byte[(int) file.length()];
                        imageInFile.read(imageData);
                        //아래 java.util.Base64.getEncoder().encodeToString(); 는
                        //인코딩의 일부로 "/" "="및 "+"를 사용하여 "standard"base64를 만듭니다. URL 안전하지 않습니다.
                        //base64Image = Base64.getEncoder().encodeToString(imageData).;
                        //따라서,import org.apache.tomcat.util.codec.binary.Base64; 를 사용.
                        base64Image = Base64.encodeBase64String(imageData);
                        imageInFile.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("Image not found" + e);
                    } catch (IOException ioe) {
                        System.out.println("Exception while reading the Image " + ioe);
                    }
                    JSONObject userInfo = new JSONObject();
                    userInfo.put("name",result[1].toString());
                    userInfo.put("belong",result[2].toString());
                    userInfo.put("certifyImg",base64Image);

                    userArray.add(userInfo);
                    jsonObject.put(result[0].toString(),userArray);


                }
                jsonObject.put("uncertified",userArray);



            }
            else{//관리자 비번이나 이메일이 잘못되었음.
                throw new BaseException(FAILED_TO_LOGIN);
            }
            //userIdx를 이용해서 jwt 토큰 생성
            String jwt = jwtService.createJwt(userIdx);
            return new PostAuthloginRes(userIdx, jwt,jsonObject); //토큰발급
        } else {
            throw new BaseException(FAILED_TO_LOGIN);
        }
        //int userIdx = user.getUserIdx();
        //String jwt = jwtService.createJwt(userIdx);

        //return new PostLoginRes(userIdx, jwt);

    }



}