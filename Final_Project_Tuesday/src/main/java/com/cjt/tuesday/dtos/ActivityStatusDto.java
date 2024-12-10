package com.cjt.tuesday.dtos;

import lombok.Data;

@Data
public class ActivityStatusDto {
    private Integer userId;
    private String username;
    private String status; // "active", "offline", "away" 등
}