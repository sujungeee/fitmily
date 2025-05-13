package com.d208.fitmily.walk.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SseService {

    private final Map<Integer, List<SseEmitter>> emitterMap = new ConcurrentHashMap<>();

    //emitter 저장 + 관리
    public SseEmitter connectFamilyEmitter(Integer familyId) {


    }
}
