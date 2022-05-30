package com.example.demo.src.Event.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@Data
public class PostPointReq {
    private double latitude;
    private double longitude;
}
