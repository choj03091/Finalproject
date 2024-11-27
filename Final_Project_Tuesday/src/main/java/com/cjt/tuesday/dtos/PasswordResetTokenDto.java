package com.cjt.tuesday.dtos;

import java.time.LocalDateTime;

public class PasswordResetTokenDto {
	private Integer userId;         // 사용자 ID
	private String token;           // 비밀번호 재설정 토큰
	private LocalDateTime expiresAt; // 만료 시간

	// Getters and Setters
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
}
