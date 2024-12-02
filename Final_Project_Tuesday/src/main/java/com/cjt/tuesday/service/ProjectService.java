package com.cjt.tuesday.service;

import com.cjt.tuesday.dtos.ProjectDto;
import com.cjt.tuesday.dtos.UserDto;
import com.cjt.tuesday.mapper.ProjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

	@Autowired
	private ProjectMapper projectMapper;

	// 사용자 ID로 프로젝트 목록 가져오기
	public List<ProjectDto> getProjectsByUserId(Integer userId) {
		return projectMapper.findProjectsByUserId(userId);
	}

	// 프로젝트 ID로 팀장 정보 가져오기
	public UserDto getTeamLeaderByProjectId(Integer projectId) {
		return projectMapper.findTeamLeader(projectId);
	}

	public List<UserDto> getTeamMembers(Integer projectId) {
		return projectMapper.findTeamMembersByProjectId(projectId);
	}

	// 프로젝트 ID로 팀원 정보 가져오기
	public List<UserDto> getTeamMembersByProjectId(Integer projectId) {
		return projectMapper.findTeamMembersByProjectId(projectId);
	}

	// 프로젝트 ID로 프로젝트 정보 가져오기
	public ProjectDto getProjectById(Integer projectId) {
		return projectMapper.findProjectById(projectId);
	}

	// 새 프로젝트 추가, 자동으로 팀장 설정.
	public void addProject(ProjectDto project, Integer userId) {
		project.setUserId(userId); // 프로젝트 생성자
		project.setTeamLeaderId(userId); // 생성자를 팀장으로 설정
		projectMapper.addProject(project);
	}
	// 프로젝트 이름 업데이트
	public void updateProjectName(Integer projectId, String name) {
		projectMapper.updateProjectName(projectId, name);
	}

	// 프로젝트 삭제
	public void deleteProject(Integer projectId) {
		projectMapper.deleteProject(projectId);
	}
}
