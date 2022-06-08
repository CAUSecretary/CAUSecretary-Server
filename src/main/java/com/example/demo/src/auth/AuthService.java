package com.example.demo.src.auth;

import com.example.demo.config.BaseException;
import com.example.demo.src.auth.model.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
//import java.util.Base64;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;



@Service
public class AuthService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${file.path}")
    String filepath;

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
            String certified = user.getCertified();
            //userIdx를 이용해서 jwt 토큰 생성
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx, jwt,certified); //토큰발급
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
        if(user.getPassword().equals(encryptPwd)) {
            //일치하면 해당 userIdx 를 받아옴.
            int userIdx = user.getUserIdx();

        //userIdx를 이용해서 jwt 토큰 생성
        String jwt = jwtService.createJwt(userIdx);
        return new PostAuthloginRes(userIdx, jwt); //토큰발급
        } else {
            throw new BaseException(FAILED_TO_LOGIN);
        }
        //int userIdx = user.getUserIdx();
        //String jwt = jwtService.createJwt(userIdx);

        //return new PostLoginRes(userIdx, jwt);

    }

    public GetAuthUncertifiedRes getUncertificationUserList()  throws BaseException {


        //String filepath = "/Users/chaehuiseon/chs_documents/caucap/befor_certification/";
        JSONArray userArray = new JSONArray();

        //미인증된 유저들을 모두가져와라
        List<Object[]> results = userRepository.sellectAllUser("F");

        for (Object[] result : results) {
            String base64Image = "";
            String filename = result[3].toString();
            System.out.println("filename : " + filename);
            File file = new File(filepath + filename);
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
            userInfo.put("userIdx", result[0].toString());//미인증 유저학번
            userInfo.put("name", result[1].toString());//미인증 유저이름
            userInfo.put("belong", result[2].toString());
            userInfo.put("certifyImg", base64Image);
            //userInfo.put("certifyImg","이미지인뎅");
            //System.out.println(userInfo.toJSONString());
            userArray.add(userInfo);

        }

        return new GetAuthUncertifiedRes(userArray);

        //GET_UNCERTIFICATION_USER_FAIL


    }

    //인증
    public String certify(int userIdx, String belong) throws BaseException{


        int check = userRepository.updatecertified("S",userIdx,belong);
        if(check == 1){
            System.out.println("update성공");
        }else{
            throw  new  BaseException(PATCH_USER_CERTIFIED_FAIL);
        }
        return "success";


    }

    //인증상태 확인
    public String checkcertified(int userIdx, String belong) throws BaseException{
        String check = userRepository.checkcertified(userIdx,belong);
        if(check.equals("F") || check.equals("S") ){
            return check;
        }else{
            throw new BaseException(CHECK_USER_CERTIFIED_FAIL);
        }
    }

    //이메일 찾기

    public String findEmail(String phone) throws BaseException{
        try{
            String findphone =  userRepository.findEmailByPhone(phone);
            return findphone;

        }catch (Exception e){
            throw  new BaseException(ERROR_FIND_EMAIL);
        }


    }


    //임시 비밀번호 발급

    public String getImsiPassword(String email) throws  BaseException{

        int userIdx = 0;
        try{
            userIdx = userRepository.findUserIdxByEmail(email);
            System.out.println("일치하는 email 존재함 : "+ userIdx);

            char[] charSet = new char[] {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

            StringBuffer sb = new StringBuffer();
            SecureRandom sr = new SecureRandom();
            sr.setSeed(new Date().getTime());

            int idx = 0;
            int len = charSet.length;
            for (int i=0; i<6; i++) {
                // idx = (int) (len * Math.random());
                idx = sr.nextInt(len);    // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
                sb.append(charSet[idx]);
            }

            String imsipassword = sb.toString();

            System.out.println("임시 비밀번호 생성 : "+ imsipassword);

            String encryptImsiPwd = new SHA256().encrypt(imsipassword);

            System.out.println(encryptImsiPwd);

            int check = userRepository.updatePassword(encryptImsiPwd,userIdx,email);
            if(check == 1){
                System.out.println("update 성공");
                return imsipassword;
            }else{
                throw  new  BaseException(PATCH_USER_PASSWORD);

            }



        }catch (Exception e){

            throw  new BaseException(ERROR_FIND_USERIDX);

        }





    }





}