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

		// 사용자 ID로 프로젝트 목록 가져오기 (직접 생성하거나 초대받은 프로젝트)
		List<ProjectDto> projects = projectService.getAccessibleProjects(user.getUserId());
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
	public String addProject(@RequestParam String name, @RequestParam(required = false) String description, HttpSession session) {
		UserDto currentUser = (UserDto) session.getAttribute("userDto");

		if (currentUser == null) {
			return "redirect:/user/login";
		}

		ProjectDto project = new ProjectDto();
		project.setName(name);
		project.setDescription(description);

		// 현재 사용자의 userId를 전달
		projectService.addProject(project, currentUser.getUserId());

		return "redirect:/project";
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
