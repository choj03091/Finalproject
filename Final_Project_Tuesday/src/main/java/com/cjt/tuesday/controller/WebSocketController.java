package com.cjt.tuesday.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.cjt.tuesday.dtos.ActivityStatusDto;

@Controller
public class WebSocketController {

    @MessageMapping("/user/status")
    @SendTo("/topic/user/status")
    public ActivityStatusDto updateUserStatus(ActivityStatusDto statusDto) {
        // 여기서 DB나 캐시에 상태를 저장할 수도 있음
        System.out.println("User status updated: " + statusDto);
        return statusDto; // 상태 변경 정보를 브로드캐스트
    }
}