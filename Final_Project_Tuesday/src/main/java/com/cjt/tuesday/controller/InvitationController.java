package com.cjt.tuesday.controller;

import com.cjt.tuesday.dtos.InvitationDto;
import com.cjt.tuesday.dtos.UserDto;
import com.cjt.tuesday.service.InvitationService;
import com.cjt.tuesday.service.ProjectService;
import com.cjt.tuesday.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/invitations")
public class InvitationController {

	@Autowired
	private InvitationService invitationService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@GetMapping("/invite")
	public String showInviteForm(@RequestParam Integer projectId, Model model) {
		model.addAttribute("projectId", projectId); // 프로젝트 ID 전달
		return "invitations/invite"; // "invitations/invite.html" 렌더링
	}


	// 초대 전송
	@PostMapping("/send")
	public String sendInvitation(
			@RequestParam("projectId") Integer projectId,
			@RequestParam("recipientEmail") String recipientEmail,
			HttpSession session,
			RedirectAttributes redirectAttributes) {

		UserDto currentUser = (UserDto) session.getAttribute("userDto");
		if (currentUser == null) {
			return "redirect:/user/login"; // 로그인 페이지로 리디렉션
		}

		// 초대 메일 전송
		invitationService.sendInvitation(projectId, currentUser.getUserId(), recipientEmail);

		// 성공 메시지를 Flash Attribute로 추가
		redirectAttributes.addFlashAttribute("inviteSuccess", "초대 메일이 전송되었습니다.");
		return "redirect:/home?projectId=" + projectId; // 쿼리 파라미터 제거
	}
	
	@GetMapping("/accept")
	public String acceptInvitation(@RequestParam("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
	    try {
	        InvitationDto invitation = invitationService.acceptInvitation(id);

	        UserDto recipient = userService.findUserByEmail(invitation.getRecipientEmail());
	        if (recipient != null) {
	            session.setAttribute("userDto", recipient);
	        }

	        redirectAttributes.addFlashAttribute("message", "프로젝트 초대를 수락하였습니다!");
	        return "redirect:/home?projectId=" + invitation.getProjectId();
	    } catch (RuntimeException e) {
	        if (e.getMessage().contains("회원가입이 필요합니다")) {
	            redirectAttributes.addFlashAttribute("error", "초대받은 이메일로 회원가입 후 다시 시도해주세요.");
	            return "redirect:/user/addUser";
	        }
	        redirectAttributes.addFlashAttribute("error", "초대 수락 중 오류가 발생했습니다.");
	        return "redirect:/project";
	    }
	}


//	@GetMapping("/accept")
//	public String acceptInvitation(
//	        @RequestParam("id") Integer id,
//	        HttpSession session,
//	        RedirectAttributes redirectAttributes) {
//	    try {
//	        // 세션에서 현재 사용자 확인
//	        UserDto currentUser = (UserDto) session.getAttribute("userDto");
//
//	        if (currentUser == null) {
//	            // 로그인 필요: 초대 정보를 Flash Attribute로 전달
//	            redirectAttributes.addFlashAttribute("inviteId", id);
//	            redirectAttributes.addFlashAttribute("message", "로그인 후 초대를 수락할 수 있습니다.");
//	            return "redirect:/user/login";
//	        }
//
//	        // 초대 수락 처리
//	        InvitationDto invitation = invitationService.acceptInvitation(id);
//
//	        // 초대된 프로젝트로 리다이렉트
//	        redirectAttributes.addFlashAttribute("message", "프로젝트 초대를 수락하였습니다!");
//	        return "redirect:/home?projectId=" + invitation.getProjectId();
//	    } catch (Exception e) {
//	        redirectAttributes.addFlashAttribute("error", "초대 수락 중 오류가 발생했습니다.");
//	        return "redirect:/project";
//	    }
//	}

	// 초대 거절
	@GetMapping("/decline")
	public String declineInvitation(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
	    try {
	        // 초대 거절 처리
	        invitationService.declineInvitation(id);

	        // 성공 메시지 추가
	        redirectAttributes.addFlashAttribute("message", "초대를 거절하였습니다.");
	    } catch (Exception e) {
	        // 오류 처리
	        redirectAttributes.addFlashAttribute("error", "초대 거절 중 오류가 발생했습니다.");
	    }
	    return "redirect:/project"; // 프로젝트 목록 페이지로 리디렉션
	}
}
