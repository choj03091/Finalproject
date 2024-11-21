package com.cjt.tuesday.controller;

import com.cjt.tuesday.dtos.UserDto;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root(HttpSession session) {
        // 세션에서 UserDto 확인
        UserDto user = (UserDto) session.getAttribute("userDto");
        if (user != null) {
            // 로그인된 사용자라면 /home으로 리디렉션
            return "redirect:/home";
        }
        // 로그인되지 않은 사용자는 /user/login으로 리디렉션
        return "redirect:/user/login";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // 세션에서 UserDto 객체 가져오기
        UserDto userDto = (UserDto) session.getAttribute("userDto");
        if (userDto == null) {
            // 로그인되지 않은 경우 로그인 페이지로 리디렉션
            return "redirect:/user/login";
        }

        // 모델에 username 추가
        model.addAttribute("username", userDto.getUsername());
        return "home"; // home.html 렌더링
    }
}
