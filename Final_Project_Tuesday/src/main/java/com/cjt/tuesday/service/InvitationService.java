package com.cjt.tuesday.service;

import com.cjt.tuesday.dtos.InvitationDto;
import com.cjt.tuesday.dtos.UserDto;
import com.cjt.tuesday.mapper.InvitationMapper;
import com.cjt.tuesday.mapper.ProjectMapper;
import com.cjt.tuesday.mapper.UserMapper;
import com.cjt.tuesday.service.EmailService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {

	@Autowired
	private InvitationMapper invitationMapper;

	@Autowired
	private ProjectMapper projectMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private EmailService emailService;

	//	// 초대 전송
	//	public void sendInvitation(Integer projectId, Integer senderId, String recipientEmail) {
	//		// 프로젝트 이름 가져오기
	//		String projectName = projectMapper.findProjectNameById(projectId);
	//
	//		// 초대 저장
	//		InvitationDto invitation = new InvitationDto();
	//		invitation.setProjectId(projectId);
	//		invitation.setSenderId(senderId);
	//		invitation.setRecipientEmail(recipientEmail);
	//		invitation.setStatus("sent");
	//
	//		// 저장 후 ID를 가져옴
	//		invitationMapper.saveInvitation(invitation);
	//		Integer invitationId = invitation.getId();		
	//
	//		// 이메일 전송
	//		String acceptUrl = "http://localhost:9090/invitations/accept?id=" + invitationId;
	//		String declineUrl = "http://localhost:9090/invitations/decline?id=" + invitationId;
	//		String subject = "프로젝트 초대: " + projectName; // 프로젝트 이름 추가
	//		String htmlContent = String.format(
	//				"<p>프로젝트 <strong>%s</strong>에 초대되었습니다.</p>" +
	//						"<a href='%s'>초대 수락</a> | <a href='%s'>초대 거절</a>", projectName, acceptUrl, declineUrl);
	//
	//		emailService.sendEmail(recipientEmail, subject, htmlContent);
	//	}

	// 새거 초대 전송
	public void sendInvitation(Integer projectId, Integer senderId, String recipientEmail) {
		String projectName = projectMapper.findProjectNameById(projectId);

		InvitationDto invitation = new InvitationDto();
		invitation.setProjectId(projectId);
		invitation.setSenderId(senderId);
		invitation.setRecipientEmail(recipientEmail);
		invitation.setStatus("sent");

		invitationMapper.saveInvitation(invitation);

		String acceptUrl = "http://192.168.22.108:9090/invitations/accept?id=" + invitation.getId();
		String declineUrl = "http://192.168.22.108:9090/invitations/decline?id=" + invitation.getId();

		String subject = "프로젝트 초대: " + projectName;
		String htmlContent = String.format(
				"<p>프로젝트 <strong>%s</strong>에 초대되었습니다.</p>" +
						"<a href='%s'>초대 수락</a> | <a href='%s'>초대 거절</a>",
						projectName, acceptUrl, declineUrl
				);

		emailService.sendEmail(recipientEmail, subject, htmlContent);
	}

	public InvitationDto acceptInvitation(Integer id) {
	    InvitationDto invitation = invitationMapper.findInvitationById(id);
	    System.out.println("SQL 실행 결과: " + invitation); // 디버깅 로그

	    if (invitation == null) {
	        throw new RuntimeException("해당 초대를 찾을 수 없습니다.");
	    }

	    if (!"sent".equals(invitation.getStatus())) {
	        throw new RuntimeException("이미 처리된 초대입니다.");
	    }

	    invitationMapper.updateInvitationStatus(id, "accepted");

	    UserDto recipient = userMapper.findUserByEmail(invitation.getRecipientEmail());
	    if (recipient == null) {
	        throw new RuntimeException("초대받은 사용자가 시스템에 등록되어 있지 않습니다.");
	    }

	    invitationMapper.addToProjectMembers(invitation.getProjectId(), recipient.getUserId());

	    return invitation;
	}

	//	public InvitationDto acceptInvitation(Integer id) {
	//		System.out.println("[Service] 초대 ID: " + id);
	//
	//		// 초대 정보 가져오기
	//		InvitationDto invitation = invitationMapper.findInvitationById(id);
	//		System.out.println("[Service] 초대 정보: " + invitation);
	//
	//
	//		// 초대 상태 업데이트
	//		invitationMapper.updateInvitationStatus(id, "accepted");
	//		System.out.println("[Service] 초대 상태 업데이트 완료.");
	//		
	//
	//
	//		// 프로젝트 멤버로 추가
	//		if (invitation.getProjectId() != null && invitation.getRecipientEmail() != null) {
	//			invitationMapper.addToProjectMembers(invitation.getProjectId(), invitation.getRecipientEmail());
	//			System.out.println("[Service] project_members 테이블에 추가 완료.");
	//		} else {
	//			throw new RuntimeException("초대 데이터에 필수 값이 누락되었습니다.");
	//		}
	//
	//	    invitationMapper.addToProjectMembers(invitation.getProjectId(), invitation.getRecipientEmail());
	//
	//		return invitation;
	//	}


	//	// 초대 수락
	//	public void acceptInvitation(Integer id) {
	//		InvitationDto invitation = invitationMapper.getInvitationById(id);
	//		if (invitation == null) {
	//			throw new IllegalArgumentException("해당 초대를 찾을 수 없습니다.");
	//		}
	//
	//		if (!"sent".equals(invitation.getStatus())) {
	//			throw new IllegalStateException("이 초대는 이미 처리되었습니다.");
	//		}
	//
	//		// 초대 상태 업데이트
	//		invitationMapper.updateInvitationStatus(id, "accepted");
	//
	//		// 초대받은 사용자를 프로젝트에 추가
	//		invitationMapper.addToProjectMembers(invitation.getProjectId(), invitation.getRecipientEmail());
	//	}

	// 초대 거절
	public void declineInvitation(Integer id) {
	    // 초대 정보 가져오기
	    InvitationDto invitation = invitationMapper.findInvitationById(id);

	    if (invitation == null) {
	        throw new RuntimeException("해당 초대를 찾을 수 없습니다.");
	    }

	    if (!"sent".equals(invitation.getStatus())) {
	        throw new RuntimeException("이미 처리된 초대입니다.");
	    }

	    // 초대 상태를 'declined'로 업데이트
	    invitationMapper.updateInvitationStatus(id, "declined");
	}	
}
