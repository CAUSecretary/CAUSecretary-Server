package com.example.demo.src.Event;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import static com.example.demo.config.BaseResponseStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.demo.src.Event.model.*;
import com.example.demo.src.auth.model.PostLoginRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.PostUserRes;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import com.example.demo.src.Event.EventRepository;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.example.demo.src.Event.EventRepository;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class EventController {


    EventRepository eventRepository;
    PhotoRepository photoRepository;
    private final EventDao eventDao;


    public EventController (EventRepository eventRepository, PhotoRepository photoRepository, EventDao eventDao){
        super();
        this.eventRepository = eventRepository;
        this.photoRepository = photoRepository;
        this.eventDao = eventDao;
    }




//    @PostMapping(value = "/event/{userIdx}")
//    public BaseResponse<PostEventRes> createEvent(Event event) throws BaseException {
//        // jwt 식별 코드 추가해야 됨.
//
//
//        System.out.println("진입");
//
//        eventRepository.save(event);
//        int userIdx = event.getUserIdx();
//        int eventIdx = eventRepository.sellectEventIdx(userIdx, event.getInstaUrl());
//        //InstagramCrawler crawler = new InstagramCrawler(Integer.toString(eventIdx), Integer.toString(event.getUserIdx()), event.getInstaUrl());
//        //crawler.start();
//        InstagramCrawler.create(Integer.toString(eventIdx), Integer.toString(event.getUserIdx()), event.getInstaUrl());
//
//        List<String> imgList = photoRepository.sellectAllImgurl(userIdx, eventIdx);
//
//        Event savedEvent = eventRepository.sellectByUserIdxAndEventIdx(userIdx, eventIdx);
//        String contents = eventRepository.sellectContents(userIdx, eventIdx);
//        System.out.println("contents: " + contents);
//        //List<Event> savedEvent = eventRepository.findAll();
//        System.out.println(savedEvent.toString());
//        PostEventRes postEventRes = new PostEventRes(
//                savedEvent.getEventIdx(),
//                savedEvent.getEventIdx(),
//                savedEvent.getPointIdx(),
//                savedEvent.getOnOff(),
//                savedEvent.getEventName(),
//                savedEvent.getBelong(),
//                savedEvent.getKakaoChatUrl(),
//                savedEvent.getPhone(),
//                savedEvent.getPeriod(),
//                //savedEvent.getContents(),
//                contents,
//                imgList);
//
//
//
//        System.out.println(postEventRes);
//
//       return new BaseResponse<>(postEventRes);
//
//
//    }
//


    @PostMapping(value = "/event/create")
    public BaseResponse<PostEventRes> createEvent(@RequestBody PostEventReq postEventReq) throws Exception, BaseException {
        // jwt 식별 코드 추가해야 됨.


        System.out.println("진입 했습니다. /event/create");
        System.out.println(postEventReq.toString()); //받은값
        int instacralwer =  postEventReq.getInstacralwer();
        //초기화
        PostEventRes rtEventRes = new PostEventRes(0,0,0,
                0,null,null,null,null,null,
                null,null,0);

        Event event = new Event();
        event.setUserIdx(postEventReq.getUserIdx());
        event.setPointIdx(postEventReq.getPointIdx());
        event.setOnOff(postEventReq.getOnOff());
        event.setEventName(postEventReq.getEventName());
        event.setBelong(postEventReq.getBelong());
        event.setKakaoChatUrl(postEventReq.getKakaoChatUrl());
        event.setPhone(postEventReq.getPhone());
        event.setPeriod(postEventReq.getPeriod());

        System.out.println(event.toString());
        System.out.println();


        if(instacralwer == 1){ //인스타 그램 사용

            //instagram을 사용할거니깐
            event.setInstaUrl(postEventReq.getInstaUrl());
            eventRepository.save(event); //event저장
            int userIdx = event.getUserIdx();
            //발급된 event idx 가져옴
            int eventIdx = eventRepository.sellectEventIdx(userIdx, event.getInstaUrl());
            System.out.println("발급된 eventIdx"+eventIdx);
            //InstagramCrawler crawler = new InstagramCrawler(Integer.toString(eventIdx), Integer.toString(event.getUserIdx()), event.getInstaUrl());
            //crawler.start();
            //크롤링 시작
            InstagramCrawler.create(Integer.toString(eventIdx), Integer.toString(event.getUserIdx()), event.getInstaUrl());
            List<String> imgList = photoRepository.sellectAllImgurl(userIdx, eventIdx);

            Event savedEvent = eventRepository.sellectByUserIdxAndEventIdx(userIdx, eventIdx);
            String contents = eventRepository.sellectContents(userIdx, eventIdx);
            System.out.println("contents: " + contents);
            //List<Event> savedEvent = eventRepository.findAll();
            System.out.println(savedEvent.toString());
            PostEventRes postEventRes = new PostEventRes(
                    savedEvent.getEventIdx(),
                    savedEvent.getUserIdx(),
                    savedEvent.getPointIdx(),
                    savedEvent.getOnOff(),
                    savedEvent.getEventName(),
                    savedEvent.getBelong(),
                    savedEvent.getKakaoChatUrl(),
                    savedEvent.getPhone(),
                    savedEvent.getPeriod(),
                    //savedEvent.getContents(),
                    contents,
                    imgList,
                    instacralwer);

            System.out.println(postEventRes);

            //return new BaseResponse<>(postEventRes);
            rtEventRes = postEventRes;


        }else if(instacralwer == 0){
            Event savedEvent = eventRepository.save(event);
            int userIdx = event.getUserIdx();
            int eventIdx = savedEvent.getEventIdx();
            PostEventRes postEventRes = new PostEventRes(
                    savedEvent.getEventIdx(),
                    savedEvent.getUserIdx(),
                    savedEvent.getPointIdx(),
                    savedEvent.getOnOff(),
                    savedEvent.getEventName(),
                    savedEvent.getBelong(),
                    savedEvent.getKakaoChatUrl(),
                    savedEvent.getPhone(),
                    savedEvent.getPeriod(),
                    //savedEvent.getContents(),
                    "",
                    null,
                    instacralwer);


            System.out.println(postEventRes);

            //return new BaseResponse<>(postEventRes);
            rtEventRes = postEventRes;


        }

        System.out.println(rtEventRes.toString());
        return new BaseResponse<>(rtEventRes);


    }



    @PatchMapping(value = "/event/post/{userIdx}", consumes = {"application/json", "text/xml"}, produces =  {"application/json", "text/xml"})
    public String patchEvent(@RequestBody PatchEventReq patchEventReq) throws BaseException{
        System.out.println("게시글 수정에 진입");
        int userIdx = patchEventReq.getUserIdx();
        int eventIdx = patchEventReq.getEventIdx();
        if ( eventDao.checkEventExist(eventIdx,userIdx) == 0){ //존재 검사
            throw new BaseException(PATCH_EVENT_EXISTS);
        }

        //존재함 ->
        String contents = patchEventReq.getContents();
        int check = eventRepository.updateContentsByUserIdxAndEventIdx(contents,userIdx,eventIdx);
        System.out.println(check);

        if(check != 1 ){ //event contents update 실패
            throw new BaseException(PATCH_EVENT_CONTENTS_FAIL);
        }

        List<String> imgs = patchEventReq.getImgs();
        for(String img : imgs){
            Photo photo = new Photo(eventIdx,userIdx,img);
            Photo savedphoto = photoRepository.save(photo);
            System.out.println(savedphoto.toString());

        }



        return "200";







    }

    







}
