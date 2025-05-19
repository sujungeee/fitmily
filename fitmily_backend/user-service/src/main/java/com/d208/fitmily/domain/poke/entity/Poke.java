package com.d208.fitmily.domain.poke.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Poke {
    private int pokeId;
    private int senderId;
    private int targetId;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}