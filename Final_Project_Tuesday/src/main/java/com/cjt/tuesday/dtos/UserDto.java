package com.cjt.tuesday.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Integer userId; // 사용자 고유 ID
    private String username; // 사용자 이름
    private String email; // 이메일 (중복 불가)
    private String password; // 암호화된 비밀번호
    private String title; // 직책 (예: 디자이너, 개발자)
    private String status; // 사용자 상태 (active, away, inactive)
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime updatedAt; // 수정 시간
}
