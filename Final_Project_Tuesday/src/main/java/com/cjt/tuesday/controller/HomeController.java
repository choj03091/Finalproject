package com.cjt.tuesday.controller;

import com.cjt.tuesday.dtos.ProjectDto;
import com.cjt.tuesday.dtos.UserDto;
import com.cjt.tuesday.service.ProjectService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
	
    @Autowired
    private ProjectService projectService;

    @GetMapping("/")
    public String root(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("userDto");
        if (user != null) {
            return "redirect:/project";
        }
        return "redirect:/user/login";
    }

    @GetMapping("/home")
    public String home(@RequestParam Integer projectId, HttpSession session, Model model) {
        UserDto currentUser = (UserDto) session.getAttribute("userDto");
        if (currentUser == null) {
            return "redirect:/user/login";
        }

        // 프로젝트 정보 가져오기
        ProjectDto project = projectService.getProjectById(projectId);
        if (project == null) {
            model.addAttribute("errorMessage", "존재하지 않는 프로젝트입니다.");
            return "redirect:/project";
        }

        // 팀장 정보 가져오기
        UserDto teamLeader = projectService.getTeamLeaderByProjectId(projectId);

     // 팀원 정보 가져오기
        List<UserDto> teamMembers = projectService.getTeamMembersByProjectId(projectId);

        
        // 모델에 데이터 추가
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("projectName", project.getName());
        model.addAttribute("teamLeader", teamLeader);
        model.addAttribute("teamMembers", teamMembers);


        return "home"; // home.html 렌더링
    }
}
