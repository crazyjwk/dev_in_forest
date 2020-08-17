package com.devinforest.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.devinforest.vo.LoginAdmin;
import com.devinforest.vo.LoginCompany;
import com.devinforest.vo.LoginMember;

@Controller
public class IndexCotroller {
	@GetMapping({"/", "/index"})
	public String index(HttpSession session,Model model) {
		String memberName = "Guest";
		String accountKind = "G";
		String companyEmail = "";
		
		if(session.getAttribute("loginMember") != null) {
			memberName = ((LoginMember)session.getAttribute("loginMember")).getMemberName();
			accountKind = ((LoginMember)session.getAttribute("loginMember")).getAccountKind();
			System.out.println(memberName + " : " + accountKind + " <---- member Session");
		} else if(session.getAttribute("loginAdmin") != null) {
			memberName = ((LoginAdmin)session.getAttribute("loginAdmin")).getAdminName();
			accountKind = ((LoginAdmin)session.getAttribute("loginAdmin")).getAccountKind();
			System.out.println(memberName + " : " + accountKind+ " <---- admin Session");
		} else if(session.getAttribute("loginCompany") != null) {
			memberName = ((LoginCompany)session.getAttribute("loginCompany")).getCompanyKorName();
			accountKind = "C";
			companyEmail = ((LoginCompany)session.getAttribute("loginCompany")).getCompanyEmail();
			System.out.println(memberName + " : " + accountKind + " : " + companyEmail +" <---- company Session");
		}
		
		
		model.addAttribute("memberName", memberName);
		model.addAttribute("accountKind", accountKind);
		model.addAttribute("companyEmail", companyEmail);
		
		return "index/index";
	}
	@GetMapping("/home")
	public String home(HttpSession session) {
		if(session.getAttribute("loginMember")==null) {
			return "member/memberLogin";
		}
		return "index/home";
	}
	@GetMapping("/companyHome")
	public String companyHome(HttpSession session) {
		if(session.getAttribute("loginCompany")==null) {
			return "member/memberLogin";
		}
		
		return "company/companyHome";
	}
}
