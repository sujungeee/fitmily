package com.d208.fitmily.walk.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalkStartDto{
    private Integer userId;
    private Integer userFamilySequence;
    private String userZodiacName;
    private String userNickname;

}
