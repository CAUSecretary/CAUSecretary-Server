package com.example.demo.src.auth;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.auth.model.GetAuthUncertifiedRes;
import com.example.demo.src.auth.model.PostAuthloginRes;
import com.example.demo.src.auth.model.PostLoginReq;
import com.example.demo.src.auth.model.PostLoginRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.PostUserReq;
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
    @Autowired
    private final UserRepository userRepository;




    public AuthController(AuthProvider authProvider, AuthService authService, JwtService jwtService, UserRepository userRepository){
        this.authProvider = authProvider;
        this.authService = authService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }



    @ResponseBody
    @PostMapping("/userlogin")
    public BaseResponse<PostLoginRes> userlogIn(@RequestBody PostLoginReq postLoginReq) throws Exception{
    //public BaseResponse<PostLoginRes> userlogIn(@RequestParam("eamil")String email, @RequestParam("password")String password) throws Exception{
//        int userIdxByJwt = jwtService.getUserIdx();
//        int userIdx = 0;
//        try{
//            userIdx = userRepository.findUserIdxByEmail(postLoginReq.getEmail());
//
//        }catch (Exception e){
//            return new BaseResponse<>(NOT_EXIST_USER);
//        }
//
//
//        if(userIdx != userIdxByJwt){
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }


        System.out.println(postLoginReq.toString());

        try{
            if(postLoginReq.getEmail() == null){ // email??? ????????? ????????? ??????
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_EMAIL);
            }
            if(postLoginReq.getPassword() == null){ // password??? ????????? ????????? ??????
                return new BaseResponse<>((POST_USERS_EMPTY_PASSWORD));
            }
            //????????? email ???????????? --> ??????, ??????????????? ???????????? ??????????????? ?????? ??????
            if(!isRegexEmail(postLoginReq.getEmail())){
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }
            PostLoginRes postLoginRes = authService.login(postLoginReq);
            System.out.println(postLoginRes.toString());

            return new BaseResponse<>(postLoginRes);

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));

        }

    }



    //????????? ?????????
    @ResponseBody
    @PostMapping("/authlogin")
    public BaseResponse<PostAuthloginRes> authlogIn(@RequestBody PostLoginReq postLoginReq) throws Exception{

        System.out.println(postLoginReq.toString());
        try{
            if(postLoginReq.getEmail() == null){ // email??? ????????? ????????? ??????
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_EMAIL);
            }
            if(postLoginReq.getPassword() == null){ // password??? ????????? ????????? ??????
                return new BaseResponse<>((POST_USERS_EMPTY_PASSWORD));
            }
            //????????? email ???????????? --> ??????, ??????????????? ???????????? ??????????????? ?????? ??????
            if(! isRegexEmail(postLoginReq.getEmail())){
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }

            PostAuthloginRes postAuthloginRes = authService.authlogin(postLoginReq);

            System.out.println("??????????????? ??????");
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
    @PatchMapping("/certify")//??????
    public BaseResponse<String> certify(@RequestParam int userIdx, @RequestParam String belong) throws BaseException {
        System.out.println(userIdx + belong);
        try{

            String check = authService.checkcertified(userIdx, belong);
            String result = "";
            if(check.equals("F")){
                //????????? ??? ????????? ????????????????????? ??????. ?????? ?????? ??????.
                result = authService.certify(userIdx,belong);

            }
            else if(check.equals("S")){
                result = "?????? ????????? ??? ??????";
            }else{
                result = "?????? ?????????";
            }
            return new BaseResponse<>(result);


        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }


    }


    //????????? ??????


    @PostMapping(value = "/users/find/email")
    @ResponseBody
    public BaseResponse<String> findUserEmail(@RequestParam String phone ) throws BaseException {
        try{
            String findemail = authService.findEmail(phone);
            System.out.println(findemail);
            return new BaseResponse<>(findemail);


        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }


    }


    //???????????? ??????

    @PatchMapping(value = "/users/find/password")
    @ResponseBody
    public BaseResponse<String> findEmail(@RequestParam("email") String email ) throws BaseException {
        String imsipassword = "";
        imsipassword = authService.getImsiPassword(email);
        System.out.println("????????? ?????? : "+ imsipassword);

        return new BaseResponse<>(imsipassword);



    }


























}
