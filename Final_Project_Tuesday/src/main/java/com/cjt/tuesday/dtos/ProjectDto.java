package com.cjt.tuesday.dtos;

import java.security.Timestamp;

import lombok.Data;

@Data
public class ProjectDto {
    private Integer id;
    private String name;
    private String description;
    private Integer userId;
    private Integer teamLeaderId; // 팀장 ID
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
