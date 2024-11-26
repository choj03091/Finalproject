package com.cjt.tuesday.service;

import com.cjt.tuesday.command.AddUserCommand;
import com.cjt.tuesday.command.LoginCommand;
import com.cjt.tuesday.dtos.UserDto;
import com.cjt.tuesday.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// 로그인 처리
	public String login(LoginCommand loginCommand, HttpServletRequest request) {
		System.out.println("로그인 시도: 이메일 = " + loginCommand.getEmail());

		UserDto userDto = userMapper.findUserByEmail(loginCommand.getEmail());
		if (userDto != null && passwordEncoder.matches(loginCommand.getPassword(), userDto.getPassword())) {
			System.out.println("로그인 성공");
			HttpSession session = request.getSession();
			session.setAttribute("userDto", userDto); // 세션에 사용자 정보 저장
			return "redirect:/home"; // 성공 시 홈으로 이동
		}

		System.out.println("로그인 실패");
		return "redirect:/user/login?error=true"; // 실패 시 로그인 페이지로 이동
	}

	// 회원가입 처리
	public void addUser(AddUserCommand addUserCommand) throws Exception {
		// 이메일 중복 확인
		if (userMapper.findUserByEmail(addUserCommand.getEmail()) != null) {
			throw new Exception("이미 존재하는 이메일입니다.");
		}

		// UserDto로 변환
		UserDto userDto = new UserDto();
		userDto.setUsername(addUserCommand.getUsername());
		userDto.setEmail(addUserCommand.getEmail());
		userDto.setPassword(passwordEncoder.encode(addUserCommand.getPassword())); // 비밀번호 암호화
		userDto.setTitle(addUserCommand.getTitle());
		userDto.setStatus("active"); // 기본 상태 설정
        userDto.setProfileColor(generateProfileColor(addUserCommand.getEmail())); // 색상 생성

		// 데이터베이스에 저장
		userMapper.addUser(userDto);
		System.out.println("회원가입 성공: " + addUserCommand.getEmail());
	}
	
	 // 사용자 고유 색상 생성
	private String generateProfileColor(String key) {
	    int hash = key.hashCode();
	    int r = (hash & 0xFF0000) >> 16; // Red
	    int g = (hash & 0x00FF00) >> 8;  // Green
	    int b = hash & 0x0000FF;         // Blue

	    // 하얀색(#FFFFFF)에 가까운 색상 방지
	    if (r > 200 && g > 200 && b > 200) {
	        r = 200; g = 180; b = 150; // 임의로 색상을 조정
	    }

	    return "#" + String.format("%02X%02X%02X", r, g, b); // #RRGGBB 형식
	}

	public boolean isEmailDuplicate(String email) {
		return userMapper.findUserByEmail(email) != null; // 이메일이 이미 존재하면 true 반환
	}
	
	public UserDto getUser(int userId) {
		return userMapper.getUser(userId);
	}
	
	public void updateUser(UserDto userDto) {
	    userMapper.updateUser(userDto);
	}
	
	public void updateUserTitle(Integer userId, String title) {
	    userMapper.updateTitle(userId, title);
	}
	
	// 비밀번호 변경 처리
    public void changePassword(String email, String currentPassword, String newPassword) throws Exception {
        UserDto userDto = userMapper.findUserByEmail(email);
        if (userDto == null) {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, userDto.getPassword())) {
            throw new Exception("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호로 업데이트
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        userMapper.updatePasswordByEmail(email, encodedNewPassword);
    }
}
