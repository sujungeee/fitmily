package com.d208.fitmily.domain.family.dto;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyCalendarResponse {
    private List<MemberInfo> members;
    private List<CalendarEntry> calendar;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberInfo {
        private int userId;
        private String userName;
        private int userFamilysequence;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CalendarEntry {
        private int userId;
        private String date;
        private String userNickname;
        private int userFamilySequence;
    }
}
