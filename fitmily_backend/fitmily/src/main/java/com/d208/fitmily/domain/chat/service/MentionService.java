//package com.d208.fitmily.chat.service;
//
//import com.d208.fitmily.chat.entity.ChatMessage;
//import com.d208.fitmily.family.mapper.FamilyMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Service
//@RequiredArgsConstructor
//public class MentionService {
//
//    private final FamilyMapper familyMapper; // 더미 매퍼 사용
//
//    public List<ChatMessage.Mention> extractMentions(String text, String familyId) {
//        List<ChatMessage.Mention> mentions = new ArrayList<>();
//        Pattern pattern = Pattern.compile("@(\\S+)");
//        Matcher matcher = pattern.matcher(text);
//
//        while (matcher.find()) {
//            String mentionedName = matcher.group(1);
//            int startIndex = matcher.start();
//            int endIndex = matcher.end();
//
//            // 간소화된 구현 - 실제로는 사용자 검색 필요
//            String userId = "dummy_" + mentionedName;
//
//            // 더미 멘션 생성
//            ChatMessage.Mention mention = new ChatMessage.Mention();
//            mention.setUserId(userId);
//            mention.setNickname(mentionedName);
//            mention.setStartIndex(startIndex);
//            mention.setEndIndex(endIndex);
//            mentions.add(mention);
//        }
//
//        return mentions;
//    }
//}