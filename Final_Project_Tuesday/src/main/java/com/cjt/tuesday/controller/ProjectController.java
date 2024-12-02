package com.cjt.tuesday.controller;

import com.cjt.tuesday.dtos.ProjectDto;
import com.cjt.tuesday.dtos.UserDto;
import com.cjt.tuesday.service.ProjectService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // 프로젝트 목록 페이지
    @GetMapping
    public String projectList(HttpSession session, Model model) {
        UserDto user = (UserDto) session.getAttribute("userDto");
        if (user == null) {
            return "redirect:/user/login";
        }

        List<ProjectDto> projects = projectService.getProjectsByUserId(user.getUserId());
        model.addAttribute("projects", projects);
        return "project"; // project.html 렌더링
    }

    // 새 프로젝트 추가 페이지
    @GetMapping("/add")
    public String addProjectPage(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("userDto");
        if (user == null) {
            return "redirect:/user/login";
        }
        return "project-add"; // project-add.html 렌더링
    }

    // 새 프로젝트 추가 처리
    @PostMapping("/add")
    public String addProject(@RequestParam String name,
                              @RequestParam(required = false) String description,
                              HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("userDto");
        if (user == null) {
            return "redirect:/user/login";
        }

        ProjectDto project = new ProjectDto();
        project.setName(name);
        project.setDescription(description);

        // 프로젝트 생성 시 팀장을 현재 사용자로 설정
        projectService.addProject(project, user.getUserId());
        return "redirect:/project"; // 프로젝트 목록으로 리디렉션
    }

    // 프로젝트 이름 수정 처리
    @PostMapping("/update")
    public String updateProject(@RequestParam Integer projectId,
                                 @RequestParam String name,
                                 HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("userDto");
        if (user == null) {
            return "redirect:/user/login";
        }

        projectService.updateProjectName(projectId, name);
        return "redirect:/project";
    }

    // 프로젝트 삭제 처리
    @PostMapping("/delete")
    public String deleteProject(@RequestParam Integer projectId, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("userDto");
        if (user == null) {
            return "redirect:/user/login";
        }

        projectService.deleteProject(projectId);
        return "redirect:/project";
    }
}
