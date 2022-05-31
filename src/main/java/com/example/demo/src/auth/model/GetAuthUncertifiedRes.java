package com.example.demo.src.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class GetAuthUncertifiedRes {

    private List<JSONObject> uncertified;

}
