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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import com.example.demo.src.Event.EventRepository;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.example.demo.src.Event.EventRepository;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    EventPointRepository eventPointRepository;
    @Autowired
    private final EventDao eventDao;


    public EventController (EventRepository eventRepository, PhotoRepository photoRepository, EventDao eventDao, EventPointRepository eventPointRepository){
        super();
        this.eventRepository = eventRepository;
        this.photoRepository = photoRepository;
        this.eventDao = eventDao;
        this.eventPointRepository = eventPointRepository;
    }


    @ResponseBody
    @PostMapping(value = "/event/create/point")
    public BaseResponse<Integer> createPoint(@RequestBody PostPointReq postPointReq){
        double latitude = postPointReq.getLatitude();
        double longitude = postPointReq.getLongitude();
        String location = postPointReq.getLocation();
        System.out.println(latitude+longitude);
        EventPoint eventPoint = new EventPoint(latitude,longitude,location);
        System.out.println(eventPoint.toString());
        EventPoint savedPoint = eventPointRepository.save(eventPoint);
        System.out.println(savedPoint.toString());
        int pointIdx = savedPoint.getPointIdx();
        return new BaseResponse<>(pointIdx);

    }





    @PostMapping(value = "/event/create")
    public BaseResponse<PostEventRes> createEvent(@RequestBody PostEventReq postEventReq) throws Exception, BaseException {
        // jwt 식별 코드 추가해야 됨.

        System.out.println("진입 했습니다. /event/create");
        System.out.println(postEventReq.toString()); //받은값
        int instacralwer =  postEventReq.getInstacralwer();
        String[] imsi = new String[1];
        Arrays.fill(imsi,"");
        //초기화
        PostEventRes rtEventRes = new PostEventRes(0,0,0,
                0,"","","","","",
                "", Arrays.asList(imsi),0);

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
            //eventIdx 발급 조건을 userIdx와, 발급 시간을 기준으로 변경.
            LocalDateTime createdAt = event.getCreatedAt();
            //발급된 event idx 가져옴
            int eventIdx = eventRepository.sellectEventIdx(userIdx, createdAt);
            //int eventIdx = eventRepository.sellectEventIdx(userIdx, event.getInstaUrl());
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
            event.setContents("");
            event.setInstaUrl("");
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
                    Arrays.asList(imsi),
                    instacralwer);


            System.out.println(postEventRes);

            //return new BaseResponse<>(postEventRes);
            rtEventRes = postEventRes;


        }

        System.out.println(rtEventRes.toString());
        return new BaseResponse<>(rtEventRes);


    }





    @PostMapping(value = "/event/create/imsi2")
    public BaseResponse<PostEventRes> createEventimsi2(@RequestBody PostEventReq postEventReq) throws Exception, BaseException {
        // jwt 식별 코드 추가해야 됨.

        System.out.println("진입 했습니다. /event/create");
        System.out.println(postEventReq.toString()); //받은값
        int instacralwer =  postEventReq.getInstacralwer();
        String[] imsi = new String[1];
        Arrays.fill(imsi,"");
        //초기화
        PostEventRes rtEventRes = new PostEventRes(0,0,0,
                0,"","","","","",
                "", Arrays.asList(imsi),0);

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
            //eventIdx 발급 조건을 userIdx와, 발급 시간을 기준으로 변경.
            LocalDateTime createdAt = event.getCreatedAt();
            //발급된 event idx 가져옴
            int eventIdx = eventRepository.sellectEventIdx(userIdx, createdAt);
            //int eventIdx = eventRepository.sellectEventIdx(userIdx, event.getInstaUrl());
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
            event.setContents("");
            event.setInstaUrl("");
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
                    Arrays.asList(imsi),
                    instacralwer);


            System.out.println(postEventRes);

            //return new BaseResponse<>(postEventRes);
            rtEventRes = postEventRes;


        }

        System.out.println(rtEventRes.toString());
        return new BaseResponse<>(rtEventRes);


    }















    @PostMapping(value = "/event/create/go")
    public BaseResponse<PostEventRes> createEventgo(@RequestParam("userIdx")int userIdx, @RequestParam("eventName") String eventName, @RequestParam("instacralwer") int instacralwer) throws Exception, BaseException {
        Event event = eventRepository.sllectAllByuserIdxandevnetName(userIdx, eventName);
        List<String> imgs = photoRepository.sellectAllImgurl(userIdx, event.getEventIdx());
        PostEventRes postEventRes =
                new PostEventRes(
                        event.getEventIdx(),
                        event.getUserIdx(),
                        event.getPointIdx(),
                        event.getOnOff(),
                        event.getEventName(),
                        event.getBelong(),
                        event.getKakaoChatUrl(),
                        event.getPhone(),
                        event.getPeriod(),
                        event.getContents(),
                        imgs,
                        instacralwer

                );
        return new BaseResponse<>(postEventRes);


    }








    @PostMapping(value = "/event/create/imsi")
    public String createEventImsi(@RequestBody PostEvent2Req postEvent2Req) throws Exception, BaseException {
        // jwt 식별 코드 추가해야 됨.

        System.out.println("진입 했습니다. /event/create");
        System.out.println(postEvent2Req.toString()); //받은값
        int instacralwer =  postEvent2Req.getInstacralwer();
        String[] imsi = new String[1];
        Arrays.fill(imsi,"");
        //초기화
        PostEventRes rtEventRes = new PostEventRes(0,0,0,
                0,"","","","","",
                "", Arrays.asList(imsi),0);

        Event event = new Event();
        event.setUserIdx(postEvent2Req.getUserIdx());
        event.setPointIdx(postEvent2Req.getPointIdx());
        event.setOnOff(postEvent2Req.getOnOff());
        event.setEventName(postEvent2Req.getEventName());
        event.setBelong(postEvent2Req.getBelong());
        event.setKakaoChatUrl(postEvent2Req.getKakaoChatUrl());
        event.setPhone(postEvent2Req.getPhone());
        event.setPeriod(postEvent2Req.getPeriod());
        event.setContents(postEvent2Req.getContents());
        event.setInstaUrl(postEvent2Req.getInstaUrl());


        System.out.println(event.toString());
        System.out.println();

        Event savedEvent = eventRepository.save(event);
        System.out.println(savedEvent.toString());

        List<String> imsi_photo = postEvent2Req.getImgs();
        for(String p : imsi_photo){
            Photo photo = new Photo();
            photo.setEventIdx(savedEvent.getEventIdx());
            photo.setUserIdx(savedEvent.getUserIdx());
            photo.setImgurl(p);
            Photo savedPhoto = photoRepository.save(photo);
            System.out.println(savedPhoto.toString());
        }


        return "저장완료";


    }







    @PatchMapping(value = "/event/post/{userIdx}", consumes = {"application/json", "text/xml"}, produces =  {"application/json", "text/xml"})
    public String patchEvent(@RequestBody PatchEventReq patchEventReq) throws BaseException{
        System.out.println("게시글 수정에 진입");
        int userIdx = patchEventReq.getUserIdx();
        int eventIdx = patchEventReq.getEventIdx();
        if ( eventDao.checkEventExist(eventIdx,userIdx) == 0){ //존재 검사
            throw new BaseException(EVENT_EXISTS);
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


//    private int userIdx; //이벤트 발급한 유저 학번
//    private int eventIdx; //이벤트 고유 아이디
//    private String eventName; //이벤트 이름
//    private String period; //이벤트 진행 기간
//    private LocalDateTime createdAt; //이벤트 생성 시간
//     @Query(value = "SELECT userIdx, eventIdx, eventName, period,createdAt From Event where userIdx = ?1",nativeQuery = true)
    @ResponseBody
    @GetMapping(value = "/get/all/evnet/{userIdx}")
    public BaseResponse<List<GetEventListRes>>showEventList(@PathVariable("userIdx") int userIdx){
        List<Object[]> results = eventRepository.showEvnetListByuserIdx(userIdx);
        System.out.println(results.toString());
        JSONArray request = new JSONArray();
         for(Object[] result : results){
             GetEventListRes getEventListRes = new GetEventListRes(
                     Integer.parseInt(result[0].toString()), //useridx
                     Integer.parseInt(result[1].toString()), //eventidx
                     result[2].toString(), //eventname
                     result[3].toString(), //period
                     result[4].toString() //createdAt
             );
             request.add(getEventListRes);


         }

        return new BaseResponse<>(request);



    }

    @ResponseBody
    @DeleteMapping(value = "/delete/evnet/{userIdx}/{eventIdx}")
    public BaseResponse<String> DeleteEvent(@PathVariable("userIdx") int userIdx, @PathVariable("eventIdx") int eventIdx) throws Exception, BaseException {
        String response = "";
        try{
            int event_check = eventRepository.deleteByUserIdxAndEventIdx(userIdx,eventIdx);
            int photo_check = photoRepository.deleteByUserIdxAndEventIdx(userIdx,eventIdx);
            System.out.println(event_check);
            System.out.println(photo_check);
            response = "success";

            return new BaseResponse<>(response);

        } catch (Exception e) {
            e.printStackTrace();
            response = "fail";

            return new BaseResponse<>(response);
        }



    }



    @ResponseBody
    @GetMapping(value = "/get/all/event/main/{OnOff}")
    public BaseResponse<List<GetMainPageRes>> showMainPage(@PathVariable("OnOff") int OnOff){
        List<Object[]> eventdatas = eventRepository.showMainPage(OnOff);
        JSONArray request = new JSONArray();
        for(Object[] eventdata : eventdatas){
            //Online
            double latitude = 0.0;
            double longitude =  0.0;
            String location = "";

            if(OnOff == 1){ //Offline
                latitude = Double.parseDouble(eventdata[5].toString());
                longitude =  Double.parseDouble(eventdata[6].toString());
                location = eventdata[7].toString();

            }
            GetMainPageRes getMainPageRes = new GetMainPageRes(
                    Integer.parseInt(eventdata[0].toString()), //eventIdx
                    eventdata[1].toString(),//eventName
                    eventdata[2].toString(), //belong
                    eventdata[3].toString(), //period
                    Integer.parseInt(eventdata[4].toString()), //pointIdx
                    latitude, longitude, location
            );
            request.add(getMainPageRes);

        }

        return new BaseResponse<>(request);


    }

    // eventIdx, eventName, belong, kakaoChatUrl, phone, period, contents , userIdx
    //eventIdx, eventName, belong, kakaoChatUrl, phone, period, contents , userIdx ,pointIdx

    @ResponseBody
    @GetMapping(value = "/get/each/event/main/{eventIdx}")
    public BaseResponse<GetEventinfoRes> showEachEventInfo(@PathVariable("eventIdx") int eventIdx) throws BaseException {
        System.out.println("진입"+eventIdx);
        if ( eventDao.checkEventExist(eventIdx) == 0){ //존재 검사
            throw new BaseException(EVENT_EXISTS);//존재안함
        }
        //존재함
        List<Object[]> event = eventRepository.showEachEvent(eventIdx);
        //초기화
        GetEventinfoRes getEventinfoRes = new GetEventinfoRes("","",
                "","","","",Arrays.asList(""),"",0);
        //eventIdx, eventName, belong, kakaoChatUrl, phone, period, contents , userIdx
        for(Object[] info : event){
            List<String> imgs ;
            if ( eventDao.checkPhotoExist(eventIdx) == 0){ //이벤트 사진 갖고 있어?없어
                imgs = new ArrayList<>(Arrays.asList("")); //빈칸
            }else{//있어
                int userIdx = Integer.parseInt(info[7].toString());//가지고와
                imgs = photoRepository.sellectAllImgurl(userIdx,eventIdx);
            }
            System.out.println("뭐 왔음????"+imgs.toString());
            System.out.println("받아온거 보여줌");
            for(Object data : info){
                System.out.println(data);
            }

            //// eventIdx, eventName, belong, kakaoChatUrl, phone, period, contents , userIdx
            /*
                String eventName; 1
                String belong; 2
            String kakaoChatUrl; 3
            String phone; 4
            String period; 5
            String contents; 6
            List<String> imgs;

             */
            System.out.println("여기까진와?");
            System.out.println("aaaaa"+
                    info[1].toString()+ info[2].toString()+
                    info[3].toString() +info[4].toString()+
                    info[5].toString() + info[6].toString()+ imgs);

            EventPoint eventPoint = eventPointRepository.selectInfo(Integer.parseInt(info[8].toString()));

            getEventinfoRes = new GetEventinfoRes(
                    info[1].toString(), info[2].toString(),
                    info[3].toString(), info[4].toString(),
                    info[5].toString(), info[6].toString(), imgs,eventPoint.getLocation(),
                    Integer.parseInt( info[8].toString()));



        }
        System.out.println("보내기 직전");
        System.out.println(getEventinfoRes.toString());
        return new BaseResponse<>(getEventinfoRes);



    }


    







}
