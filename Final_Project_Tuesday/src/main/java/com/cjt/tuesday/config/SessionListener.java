package com.cjt.tuesday.config;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cjt.tuesday.mapper.UserMapper;
import com.cjt.tuesday.dtos.UserDto;

@Component
public class SessionListener implements HttpSessionListener {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        UserDto userDto = (UserDto) se.getSession().getAttribute("userDto");

        if (userDto != null) {
            // 세션 만료 시 상태를 inactive로 변경
            userMapper.updateStatus(userDto.getUserId(), "inactive");
            System.out.println("세션 만료: 사용자 상태를 inactive로 설정");
        }
    }
}
