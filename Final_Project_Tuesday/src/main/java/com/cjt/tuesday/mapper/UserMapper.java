package com.cjt.tuesday.mapper;

import com.cjt.tuesday.dtos.PasswordResetTokenDto;
import com.cjt.tuesday.dtos.UserDto;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface UserMapper {

	// 이메일로 사용자 조회
	UserDto findUserByEmail(String email);

	// 사용자 추가 (회원가입)
	void addUser(UserDto userDto);

	void updateUser(UserDto userDto);

	public UserDto getUser(int userId);
	

	void updateTitle(@Param("userId") Integer userId, @Param("title") String title);

	void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

	// 비밀번호 재설정 토큰 저장
	void savePasswordResetToken(@Param("userId") Integer userId, 
			@Param("token") String token, 
			@Param("expiresAt") LocalDateTime expiresAt);

	// 토큰으로 사용자 ID 조회
	Integer findUserIdByToken(String token);

	PasswordResetTokenDto findPasswordResetToken(String token);
	
	int updatePassword(@Param("userId") Integer userId, @Param("password") String password);
	
	void deletePasswordResetToken(String token);



}

