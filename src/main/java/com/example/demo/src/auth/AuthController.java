package com.example.demo.src.auth;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.auth.model.GetAuthUncertifiedRes;
import com.example.demo.src.auth.model.PostAuthloginRes;
import com.example.demo.src.auth.model.PostLoginReq;
import com.example.demo.src.auth.model.PostLoginRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/auth")
public class AuthController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AuthProvider authProvider;
    @Autowired
    private final AuthService authService;
    @Autowired
    private final JwtService jwtService;




    public AuthController(AuthProvider authProvider, AuthService authService, JwtService jwtService){
        this.authProvider = authProvider;
        this.authService = authService;
        this.jwtService = jwtService;
    }



    @ResponseBody
    @PostMapping("/userlogin")
    public BaseResponse<PostLoginRes> userlogIn(@RequestBody PostLoginReq postLoginReq) throws Exception{

        System.out.println(postLoginReq.toString());
        try{
            if(postLoginReq.getEmail() == null){ // email이 비어서 왔는지 확인
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_EMAIL);
            }
            if(postLoginReq.getPassword() == null){ // password가 비어서 왔는지 확인
                return new BaseResponse<>((POST_USERS_EMPTY_PASSWORD));
            }
            //올바른 email 형식인지 --> 후에, 중앙대학교 메일로만 받는것으로 수정 예정
            if(!isRegexEmail(postLoginReq.getEmail())){
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }

            PostLoginRes postLoginRes = authService.login(postLoginReq);

            return new BaseResponse<>(postLoginRes);

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));

        }

    }



    //관리자 로그인
    @ResponseBody
    @PostMapping("/authlogin")
    public BaseResponse<PostAuthloginRes> authlogIn(@RequestBody PostLoginReq postLoginReq) throws Exception{

        System.out.println(postLoginReq.toString());
        try{
            if(postLoginReq.getEmail() == null){ // email이 비어서 왔는지 확인
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_EMAIL);
            }
            if(postLoginReq.getPassword() == null){ // password가 비어서 왔는지 확인
                return new BaseResponse<>((POST_USERS_EMPTY_PASSWORD));
            }
            //올바른 email 형식인지 --> 후에, 중앙대학교 메일로만 받는것으로 수정 예정
            if(! isRegexEmail(postLoginReq.getEmail())){
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }

            PostAuthloginRes postAuthloginRes = authService.authlogin(postLoginReq);

            System.out.println("보내기직전 잘감");
            System.out.println(postAuthloginRes.getJwt());
            return new BaseResponse<>(postAuthloginRes);

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));

        }

    }
    @ResponseBody
    @PostMapping("/uncertified")
    public BaseResponse<GetAuthUncertifiedRes> getUncertificationUserList(@RequestParam int userIdx) throws BaseException {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetAuthUncertifiedRes getAuthUncertifiedRes = authService.getUncertificationUserList();

            return new BaseResponse<>(getAuthUncertifiedRes);

        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }


    @ResponseBody
    @PatchMapping("/certify")//인증
    public BaseResponse<String> certify(@RequestParam int userIdx, @RequestParam String belong) throws BaseException {
        System.out.println(userIdx + belong);
        try{

            String check = authService.checkcertified(userIdx, belong);
            String result = "";
            if(check.equals("F")){
                //미인증 된 유저가 인증신청한것을 확인. 인증 절차 진행.
                result = authService.certify(userIdx,belong);

            }
            else if(check.equals("S")){
                result = "이미 인증이 된 유저";
            }else{
                result = "뭔가 잘못됨";
            }
            return new BaseResponse<>(result);


        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }


    }

















}
