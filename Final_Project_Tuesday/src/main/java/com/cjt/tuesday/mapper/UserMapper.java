package com.cjt.tuesday.mapper;

import com.cjt.tuesday.dtos.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface UserMapper {

	// 이메일로 사용자 조회
	UserDto findUserByEmail(String email);
	
	// 사용자 추가 (회원가입)
	void addUser(UserDto userDto);
	
	void updateUser(UserDto userDto);
	
}

