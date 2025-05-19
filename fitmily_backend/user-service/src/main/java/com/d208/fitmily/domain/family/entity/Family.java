package com.d208.fitmily.domain.family.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Family {
    private int familyId;
    private String familyName;
    private String familyInviteCode;
    private int familyPeople;
    private LocalDateTime familyCreatedAt;
    private LocalDateTime familyUpdatedAt;
}