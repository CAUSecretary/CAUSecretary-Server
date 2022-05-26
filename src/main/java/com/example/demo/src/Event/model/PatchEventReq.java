package com.example.demo.src.Event.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@Data
public class PatchEventReq {

    private int eventIdx;
    private int userIdx;
    private String contents;
    private List<String> imgs;

}


