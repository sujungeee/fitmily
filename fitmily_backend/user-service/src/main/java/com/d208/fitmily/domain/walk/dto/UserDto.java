package com.d208.fitmily.domain.walk.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 산책중인 가족 구성원 정보 DTO
public class UserDto {
    private Integer userId;
    private Integer userFamilySequence;
    private String userZodiacName;
    private String userNickname;
    private Integer familyId;

}
