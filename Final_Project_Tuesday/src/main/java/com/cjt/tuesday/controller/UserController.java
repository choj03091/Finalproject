package com.cjt.tuesday.controller;

import com.cjt.tuesday.command.AddUserCommand;
import com.cjt.tuesday.command.LoginCommand;
import com.cjt.tuesday.dtos.UserDto;
import com.cjt.tuesday.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 로그인 폼 이동
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginCommand", new LoginCommand());
        return "user/login"; // 로그인 폼 렌더링
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@Validated LoginCommand loginCommand,
                        BindingResult result,
                        Model model,
                        HttpServletRequest request) {
        if (result.hasErrors()) {
            System.out.println("로그인 유효성 검사 실패");
            return "user/login";
        }

        String redirectPath = userService.login(loginCommand, request);
        return redirectPath; // 로그인 성공 또는 실패에 따라 경로 반환
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        System.out.println("로그아웃 처리");
        session.invalidate(); // 세션 무효화
        return "redirect:/user/login";
    }

    // 회원가입 폼 이동
    @GetMapping("/addUser")
    public String addUserForm(Model model) {
        System.out.println("회원가입 폼 이동");
        model.addAttribute("addUserCommand", new AddUserCommand());
        return "user/addUserForm"; // 회원가입 폼 렌더링
    }

    // 회원가입 처리
    @PostMapping("/addUser")
    public String addUser(@Validated AddUserCommand addUserCommand,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            System.out.println("회원가입 유효성 검사 실패");
            return "user/addUserForm"; // 유효성 검사 실패 시 회원가입 폼으로 돌아감
        }

        try {
            userService.addUser(addUserCommand);
            System.out.println("회원가입 성공");
            return "redirect:/user/login"; // 회원가입 성공 후 로그인 페이지로 이동
        } catch (Exception e) {
            System.out.println("회원가입 중 오류 발생");
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
            e.printStackTrace();
            return "user/addUserForm";
        }
    }
    
    @GetMapping("/emailChk")
    @ResponseBody
    public Map<String, Boolean> emailChk(@RequestParam String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return response;
    }
    
    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("userDto");
        System.out.println("세션의 사용자 정보: " + session.getAttribute("userDto"));

        if (userDto == null) {
            return "redirect:/user/login"; // 로그인되지 않은 경우
        }
        model.addAttribute("user", userDto); // user 객체 추가
        return "user/profile";
    }
    
    @PostMapping("/profile")
    public String updateProfile(@Validated UserDto updatedUser,
                                BindingResult result,
                                HttpSession session,
                                Model model) {
        if (result.hasErrors()) {
            return "user/profile"; // 유효성 검사 실패 시 다시 프로필 페이지로 이동
        }

        // 로그인한 사용자 정보 가져오기
        UserDto userDto = (UserDto) session.getAttribute("userDto");
        if (userDto == null) {
            return "redirect:/user/login"; // 세션 만료 또는 비로그인 상태
        }

        // 사용자 정보 업데이트 (서비스 호출)
        try {
            userDto.setUsername(updatedUser.getUsername());
            userDto.setTitle(updatedUser.getTitle());
            userService.updateUser(userDto);

            // 세션 정보도 업데이트
            session.setAttribute("userDto", userDto);
            model.addAttribute("successMessage", "프로필이 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "프로필 업데이트 중 오류가 발생했습니다.");
        }

        return "user/profile";
    }

    
}
