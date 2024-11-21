package com.cjt.tuesday.controller;

import com.cjt.tuesday.command.AddUserCommand;
import com.cjt.tuesday.command.LoginCommand;
import com.cjt.tuesday.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
