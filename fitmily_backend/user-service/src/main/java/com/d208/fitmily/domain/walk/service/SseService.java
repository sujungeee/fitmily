package com.d208.fitmily.domain.walk.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    private final Map<Integer, List<SseEmitter>> emitterMap = new ConcurrentHashMap<>();

    // emitter 삭제
    private void removeEmitter(Integer familyId, SseEmitter emitter) {
        List<SseEmitter> emitters = emitterMap.get(familyId);
        if (emitters != null) {
            emitters.remove(emitter);
        }
    }

    //emitter 저장
    public SseEmitter connectFamilyEmitter(Integer familyId) {
        //Long.MAX_VALUE 무한 연결 유지
        //새로운 sse연결 객체 생성
        SseEmitter emitter = new SseEmitter(8 * 60 * 1000L);

        //패밀리 ID 별로 emitterList를 만들고, familyId에 맞게 emitter를 넣음
        List<SseEmitter> emitterList = emitterMap.get(familyId);
        if (emitterList == null) {
            emitterList = new CopyOnWriteArrayList<>();   //CopyOnWriteArrayList 동시접근에 좀 안전한 배열이래요
            emitterMap.put(familyId, emitterList);
        }
        emitterList.add(emitter);

        // 연결끊어지거나, 타임아웃, 에러나면 emitter 삭제시켜줌 ( 메모리 아끼는거지)
        emitter.onCompletion(() -> removeEmitter(familyId, emitter));
        emitter.onTimeout(() -> removeEmitter(familyId, emitter));
        emitter.onError(e -> removeEmitter(familyId, emitter));

        return emitter;
    }

    public void sendFamilyWalkingEvent(Integer familyId, Object data) {
        List<SseEmitter> emitters = emitterMap.get(familyId);
        if (emitters == null) return;

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("walkStart")
                        .data(data));
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }
    }

}
