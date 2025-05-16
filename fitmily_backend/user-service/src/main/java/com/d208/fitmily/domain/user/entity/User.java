package com.d208.fitmily.domain.user.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
public class User {
    private Integer userId;
    private Integer familyId;
    private String loginId;
    private String password;
    private String nickname;
    private String birth;
    private Integer gender;
    private String refreshToken;
    private String zodiacName;
    private Integer familySequence;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getUserNickname() {
        return nickname;
    }

    public String getUserZodiacName() {
        return zodiacName;
    }

    public int getUserFamilySequence() {
        return familySequence;
    }

    public String getUserBirth() {
        return birth;
    }

    public int getUserGender() {
        return gender;
    }
}