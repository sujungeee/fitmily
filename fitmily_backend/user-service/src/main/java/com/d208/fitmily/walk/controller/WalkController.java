package com.d208.fitmily.walk.controller;


import com.d208.fitmily.walk.service.WalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api")
public class WalkController {

    private final WalkService walkService;

    //산책 중지


    //산책기록 조회

    //산책 기록 상세 조회

    //산책 목표 존재 여부 조회


}
