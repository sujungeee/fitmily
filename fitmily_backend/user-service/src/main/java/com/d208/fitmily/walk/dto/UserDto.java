package com.d208.fitmily.walk.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Integer userId;
    private String name;
    private String profileImg;
    // 필요하면 추가 필드 (예: age, gender 등)
}
