//package com.d208.fitmily.domain.chat.controller;
//
//import com.d208.fitmily.domain.chat.dto.MessageRequestDTO;
//import com.d208.fitmily.domain.chat.dto.ReadReceiptRequestDTO;
//import com.d208.fitmily.domain.chat.entity.ChatMessage;
//import com.d208.fitmily.domain.chat.service.ChatMessageService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//
//import java.security.Principal;
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//@RequiredArgsConstructor
//@Slf4j
//public class ChatMessageController {
//
//    private final ChatMessageService chatMessageService;
//    private final SimpMessagingTemplate messagingTemplate;
//
//    @MessageMapping("/chat.sendMessage/family/{familyId}")
//    public void sendMessage(
//            @DestinationVariable String familyId,
//            @Payload MessageRequestDTO messageRequest,
//            Principal principal,
//            SimpMessageHeaderAccessor headerAccessor) {
//
//        log.info("메시지 송신 - familyId: {}, content: {}, type: {}",
//                familyId, messageRequest.getContent(), messageRequest.getMessageType());
//
//        // 사용자 ID 추출
//        String userId = extractUserId(principal, headerAccessor);
//        log.info("추출된 사용자 ID: {}", userId);
//
//        if (userId != null) {
//            // 권한 검증
//            if (chatMessageService.validateUserFamilyAccess(userId, familyId)) {
//                try {
//                    // 1. 메시지 저장
//                    ChatMessage chatMessage = chatMessageService.saveMessageOnly(familyId, messageRequest, userId);
//
//                    // 2. 브로드캐스트 메시지 준비
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("type", "CHAT_MESSAGE");
//                    response.put("data", chatMessage);
//
//                    // 3. 브로드캐스트 전송
//                    String destination = "/topic/chat/family/" + familyId;
//                    log.info("브로드캐스트 시작 - destination: {}, messageId: {}",
//                            destination, chatMessage.getMessageId());
//                    messagingTemplate.convertAndSend(destination, response);
//                    log.info("브로드캐스트 완료 - destination: {}, messageId: {}",
//                            destination, chatMessage.getMessageId());
//
//                    // 4. FCM 알림 전송 (브로드캐스트 이후)
//                    log.info("FCM 알림 전송 시작 - familyId: {}, messageId: {}",
//                            familyId, chatMessage.getMessageId());
//                    chatMessageService.sendFCMNotifications(familyId, chatMessage);
//                    log.info("FCM 알림 전송 완료 - familyId: {}, messageId: {}",
//                            familyId, chatMessage.getMessageId());
//
//                } catch (Exception e) {
//                    log.error("메시지 처리 중 오류: {}", e.getMessage(), e);
//                    Map<String, Object> errorResponse = new HashMap<>();
//                    errorResponse.put("type", "ERROR");
//                    errorResponse.put("message", "메시지 처리 중 오류 발생: " + e.getMessage());
//                    messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
//                }
//            } else {
//                log.error("사용자가 해당 패밀리에 접근할 권한이 없습니다 - userId: {}, familyId: {}", userId, familyId);
//                Map<String, Object> errorResponse = new HashMap<>();
//                errorResponse.put("type", "ERROR");
//                errorResponse.put("message", "접근 권한이 없습니다");
//                messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
//            }
//        } else {
//            log.error("사용자 인증 정보를 찾을 수 없음");
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("type", "ERROR");
//            errorResponse.put("message", "인증 정보를 찾을 수 없습니다");
//            messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
//        }
//    }
//
//    @MessageMapping("/chat.markAsRead/family/{familyId}")
//    public void markAsRead(
//            @DestinationVariable String familyId,
//            @Payload ReadReceiptRequestDTO readReceiptRequest,
//            Principal principal,
//            SimpMessageHeaderAccessor headerAccessor) {
//
//        log.info("읽음 상태 수신 - familyId: {}, messageId: {}",
//                familyId, readReceiptRequest.getMessageId());
//
//        // 사용자 ID 추출
//        String userId = extractUserId(principal, headerAccessor);
//
//        if (userId != null) {
//            if (chatMessageService.validateUserFamilyAccess(userId, familyId)) {
//                try {
//                    // 1. 읽음 상태 처리
//                    Map<String, Object> readStatus = chatMessageService.processMarkAsRead(
//                            familyId, readReceiptRequest.getMessageId(), userId);
//
//                    // 2. 브로드캐스트 전송
//                    String destination = "/topic/chat/family/" + familyId;
//                    log.info("읽음 상태 브로드캐스트 시작 - destination: {}", destination);
//                    messagingTemplate.convertAndSend(destination, readStatus);
//                    log.info("읽음 상태 브로드캐스트 완료 - destination: {}", destination);
//                } catch (Exception e) {
//                    log.error("읽음 상태 처리 중 오류: {}", e.getMessage(), e);
//                    Map<String, Object> errorResponse = new HashMap<>();
//                    errorResponse.put("type", "ERROR");
//                    errorResponse.put("message", "읽음 상태 처리 중 오류 발생: " + e.getMessage());
//                    messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
//                }
//            } else {
//                log.error("사용자가 해당 패밀리에 접근할 권한이 없습니다 - userId: {}, familyId: {}", userId, familyId);
//                Map<String, Object> errorResponse = new HashMap<>();
//                errorResponse.put("type", "ERROR");
//                errorResponse.put("message", "접근 권한이 없습니다");
//                messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
//            }
//        } else {
//            log.error("사용자 인증 정보를 찾을 수 없음");
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("type", "ERROR");
//            errorResponse.put("message", "인증 정보를 찾을 수 없습니다");
//            messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
//        }
//    }
//
//    // 사용자 ID 추출 메서드
//    private String extractUserId(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
//        // 1. 세션 속성에서 userId 바로 추출 (가장 안정적)
//        if (headerAccessor != null && headerAccessor.getSessionAttributes() != null) {
//            Object userIdAttr = headerAccessor.getSessionAttributes().get("userId");
//            if (userIdAttr instanceof String) {
//                return (String) userIdAttr;
//            }
//        }
//
//        // 2. Authentication에서 추출
//        if (principal instanceof Authentication auth) {
//            return auth.getName();
//        }
//
//        // 3. 헤더 액세서의 User에서 추출
//        if (headerAccessor != null && headerAccessor.getUser() != null) {
//            return headerAccessor.getUser().getName();
//        }
//
//        return null;
//    }
//}

package com.d208.fitmily.domain.chat.controller;

import com.d208.fitmily.domain.chat.dto.MessageRequestDTO;
import com.d208.fitmily.domain.chat.dto.ReadReceiptRequestDTO;
import com.d208.fitmily.domain.chat.entity.ChatMessage;
import com.d208.fitmily.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage/family/{familyId}")
    public void sendMessage(
            @DestinationVariable String familyId,
            @Payload MessageRequestDTO messageRequest,
            Principal principal,
            SimpMessageHeaderAccessor headerAccessor) {

        log.info("메시지 송신 - familyId: {}, content: {}, type: {}",
                familyId, messageRequest.getContent(), messageRequest.getMessageType());

        // 사용자 ID 추출
        String userId = extractUserId(principal, headerAccessor);
        log.info("추출된 사용자 ID: {}", userId);

        if (userId != null) {
            // 권한 검증
            if (chatMessageService.validateUserFamilyAccess(userId, familyId)) {
                try {
                    // 1. 메시지 저장
                    ChatMessage chatMessage = chatMessageService.saveMessageOnly(familyId, messageRequest, userId);

                    // 2. 브로드캐스트 메시지 준비
                    Map<String, Object> response = new HashMap<>();
                    response.put("type", "CHAT_MESSAGE");
                    response.put("data", chatMessage);

                    // 3. 브로드캐스트 전송 - 신뢰성 향상을 위해 헤더 추가
                    String destination = "/topic/chat/family/" + familyId;
                    log.info("브로드캐스트 시작 - destination: {}, messageId: {}",
                            destination, chatMessage.getMessageId());

                    // 추가 헤더를 통해 메시지 식별을 향상
                    Map<String, Object> headers = new HashMap<>();
                    headers.put("messageId", chatMessage.getMessageId());
                    headers.put("timestamp", chatMessage.getSentAt().getTime());

                    messagingTemplate.convertAndSend(destination, response, headers);
                    log.info("브로드캐스트 완료 - destination: {}, messageId: {}",
                            destination, chatMessage.getMessageId());

                    // 4. FCM 알림 전송 (브로드캐스트 이후)
                    log.info("FCM 알림 전송 시작 - familyId: {}, messageId: {}",
                            familyId, chatMessage.getMessageId());
                    chatMessageService.sendFCMNotifications(familyId, chatMessage);
                    log.info("FCM 알림 전송 완료 - familyId: {}, messageId: {}",
                            familyId, chatMessage.getMessageId());

                } catch (Exception e) {
                    log.error("메시지 처리 중 오류: {}", e.getMessage(), e);
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("type", "ERROR");
                    errorResponse.put("message", "메시지 처리 중 오류 발생: " + e.getMessage());
                    messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
                }
            } else {
                log.error("사용자가 해당 패밀리에 접근할 권한이 없습니다 - userId: {}, familyId: {}", userId, familyId);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("type", "ERROR");
                errorResponse.put("message", "접근 권한이 없습니다");
                messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
            }
        } else {
            log.error("사용자 인증 정보를 찾을 수 없음");
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "ERROR");
            errorResponse.put("message", "인증 정보를 찾을 수 없습니다");
            messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
        }
    }

    @MessageMapping("/chat.markAsRead/family/{familyId}")
    public void markAsRead(
            @DestinationVariable String familyId,
            @Payload ReadReceiptRequestDTO readReceiptRequest,
            Principal principal,
            SimpMessageHeaderAccessor headerAccessor) {

        log.info("읽음 상태 수신 - familyId: {}, messageId: {}",
                familyId, readReceiptRequest.getMessageId());

        // 사용자 ID 추출
        String userId = extractUserId(principal, headerAccessor);

        if (userId != null) {
            if (chatMessageService.validateUserFamilyAccess(userId, familyId)) {
                try {
                    // 1. 읽음 상태 처리
                    Map<String, Object> readStatus = chatMessageService.processMarkAsRead(
                            familyId, readReceiptRequest.getMessageId(), userId);

                    // 2. 브로드캐스트 전송
                    String destination = "/topic/chat/family/" + familyId;
                    log.info("읽음 상태 브로드캐스트 시작 - destination: {}", destination);
                    messagingTemplate.convertAndSend(destination, readStatus);
                    log.info("읽음 상태 브로드캐스트 완료 - destination: {}", destination);
                } catch (Exception e) {
                    log.error("읽음 상태 처리 중 오류: {}", e.getMessage(), e);
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("type", "ERROR");
                    errorResponse.put("message", "읽음 상태 처리 중 오류 발생: " + e.getMessage());
                    messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
                }
            } else {
                log.error("사용자가 해당 패밀리에 접근할 권한이 없습니다 - userId: {}, familyId: {}", userId, familyId);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("type", "ERROR");
                errorResponse.put("message", "접근 권한이 없습니다");
                messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
            }
        } else {
            log.error("사용자 인증 정보를 찾을 수 없음");
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "ERROR");
            errorResponse.put("message", "인증 정보를 찾을 수 없습니다");
            messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, errorResponse);
        }
    }

    // 사용자 ID 추출 메서드
    private String extractUserId(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        // 1. 세션 속성에서 userId 바로 추출 (가장 안정적)
        if (headerAccessor != null && headerAccessor.getSessionAttributes() != null) {
            Object userIdAttr = headerAccessor.getSessionAttributes().get("userId");
            if (userIdAttr instanceof String) {
                return (String) userIdAttr;
            }
        }

        // 2. Authentication에서 추출
        if (principal instanceof Authentication auth) {
            return auth.getName();
        }

        // 3. 헤더 액세서의 User에서 추출
        if (headerAccessor != null && headerAccessor.getUser() != null) {
            return headerAccessor.getUser().getName();
        }

        return null;
    }
}