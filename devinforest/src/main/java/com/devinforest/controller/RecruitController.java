package com.devinforest.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.devinforest.service.ApplyService;
import com.devinforest.service.CompanyService;
import com.devinforest.service.RecruitService;
import com.devinforest.vo.Apply;
import com.devinforest.vo.Company;
import com.devinforest.vo.LoginCompany;
import com.devinforest.vo.Recruit;

@Controller
public class RecruitController {
	@Autowired	
	private CompanyService companyService;
	@Autowired
	private RecruitService recruitService;
	@Autowired
	private ApplyService applyService;
	//기업 채용공고 보기
		@GetMapping("/getRecruitListByCompany")
		public String getRecruitListByCompany(HttpSession session,Model model,
				   @RequestParam(defaultValue = "1") int currentPage,
				   @RequestParam(defaultValue = "5") int rowPerPage,String searchWord) {
			if(session.getAttribute("loginCompany")==null) {
				return "redirect:/index";
			}
			
			Map<String, Object> map = recruitService.getRecruitListByCompany(currentPage, rowPerPage, searchWord);
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("rowPerPage", rowPerPage);
			model.addAttribute("searchWord", searchWord);
			model.addAttribute("lastPage", map.get("lastPage"));
			model.addAttribute("recruitTotalCount", map.get("recruitTotalCount"));
			model.addAttribute("recruitList", map.get("recruitList"));
			return "company/getRecruitListByCompany";
		}
	//채용공고 수정
		@GetMapping("/modifyRecruit")
		public String modifyRecruit(HttpSession session, Model model, int recruitNo) {
			if(session.getAttribute("loginCompany")==null) {
		         return "redirect:/index";
		      }
			Recruit recruit = recruitService.getRecruitInfo(recruitNo);
			model.addAttribute("recruit", recruit);
			return "company/modifyRecruit";
		}
		@PostMapping("/modifyRecruit")
		public String modifyRecruit(HttpSession session, Model model,
				@RequestParam(defaultValue = "1") int currentPage,
				   @RequestParam(defaultValue = "5") int rowPerPage,
				   @RequestParam(defaultValue = "") String searchWord,Recruit recruit) {
			if(session.getAttribute("loginCompany")==null) {
		         return "redirect:/index";
		      }
			recruitService.modifyRecruit(recruit);
			Recruit checkRecruit = recruitService.getRecruitInfo(recruit.getRecruitNo());
			
			model.addAttribute("recruit", checkRecruit);
			return "company/getRecruitInfoByCompany";
		}
	//채용공고 추가
	   @GetMapping("/addRecruit")
	   public String addRecruit(HttpSession session, Model model, String companyEmail) {
	      if(session.getAttribute("loginCompany")==null) {
	         return "redirect:/index";
	      }
	      System.out.println(companyEmail+"<-----addRecruit");
	      LoginCompany loginCompany= new LoginCompany();
	      loginCompany.setCompanyEmail(companyEmail);
	      Company company = companyService.getCompanyInfo(loginCompany);
	      System.out.println(company+"<---addRecruit Class");
	      model.addAttribute("company", company);
	      return "company/addRecruit";
	   }
	   @PostMapping("/addRecruit")
	   public String addRecruit(HttpSession session, Model model,
			   @RequestParam(defaultValue = "1") int currentPage,
			   @RequestParam(defaultValue = "5") int rowPerPage,
			   @RequestParam(defaultValue = "") String searchWord, Recruit recruit) {
	      System.out.println(recruit+"<---addRecruit(Post)");
	      recruitService.addRecruit(recruit);
	      searchWord = recruit.getCompanyKorName();
	      Map<String, Object> map = recruitService.getRecruitListByCompany(currentPage, rowPerPage, searchWord);
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("rowPerPage", rowPerPage);
			model.addAttribute("searchWord", searchWord);
			model.addAttribute("lastPage", map.get("lastPage"));
			model.addAttribute("recruitTotalCount", map.get("recruitTotalCount"));
			model.addAttribute("recruitList", map.get("recruitList"));
	      return "company/getRecruitListByCompany";
	   }
	 //채용공고 삭제
		@PostMapping("/removeRecruit")
		public String removeRecruit(HttpSession session,Recruit recruit, Model model,@RequestParam(defaultValue = "1") int currentPage,
				   @RequestParam(defaultValue = "5") int rowPerPage, int recruitNo) {
			if(session.getAttribute("loginCompany")==null) {
				return "redirect:/";
			}
			recruit = recruitService.getRecruitInfo(recruitNo);
			model.addAttribute("recruit", recruit);
			System.out.println(recruit+"<----recruitController");
			recruitService.removeRecruit(recruit);
			String searchWord = recruit.getCompanyKorName();
			Map<String, Object> map = recruitService.getRecruitListByCompany(currentPage, rowPerPage, searchWord);
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("rowPerPage", rowPerPage);
			model.addAttribute("searchWord", searchWord);
			model.addAttribute("lastPage", map.get("lastPage"));
			model.addAttribute("recruitTotalCount", map.get("recruitTotalCount"));
			model.addAttribute("recruitList", map.get("recruitList"));
			return "company/getRecruitListByCompany";
			
		}
	//채용공고 리스트
	@GetMapping("/getRecruitList")
	public String getRecruitList(HttpSession session, 
							   Model model,
							   @RequestParam(defaultValue = "1") int currentPage,
							   @RequestParam(defaultValue = "5") int rowPerPage,
							   @RequestParam(defaultValue = "") String searchWord) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/index";
		}
		Map<String, Object> map = recruitService.getRecruitList(currentPage, rowPerPage, searchWord);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("rowPerPage", rowPerPage);
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("lastPage", map.get("lastPage"));
		model.addAttribute("recruitTotalCount", map.get("recruitTotalCount"));
		model.addAttribute("recruitList", map.get("recruitList"));
		return "member/getRecruitList";
	}
	//채용공고  상세보기
	@GetMapping("/getRecruitInfo")
	public String getRecruitInfo(HttpSession session, Model model,
			   @RequestParam(defaultValue = "1") int currentPage,
			   @RequestParam(defaultValue = "5") int rowPerPage, int recruitNo,
			   @RequestParam(defaultValue = "") String memberName,
			   @RequestParam(defaultValue = "") String suggestType) {
		Recruit recruit = recruitService.getRecruitInfo(recruitNo);
		System.out.println(recruit+"getRecruitInfo");
		model.addAttribute("recruit", recruit);
		System.out.println(memberName+"<---getRecruitInfo");
		System.out.println(suggestType+"<---getRecruitInfo");
		
		if(session.getAttribute("loginMember")!=null) {
			model.addAttribute("recruitNo", recruitNo);
			model.addAttribute("memberName", memberName);
			model.addAttribute("suggestType", suggestType);
			return "member/getRecruitInfo";
		}
		if(session.getAttribute("loginCompany")!=null) {
			
			Map<String, Object> map = applyService.getApplyList(currentPage, rowPerPage, recruitNo);
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("rowPerPage", rowPerPage);
			model.addAttribute("lastPage", map.get("lastPage"));
			model.addAttribute("applyTotalCount", map.get("applyTotalCount"));
			model.addAttribute("applyList", map.get("applyList"));
			return "company/getRecruitInfoByCompany";
		}
		return "redirect:/index";
	}
	@PostMapping("/addApply")
	public String addApply(HttpSession session, Model model, Apply apply) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/index";
		}
		Recruit recruit = recruitService.getRecruitInfo(apply.getRecruitNo());
		System.out.println(apply+"<---postapply");
		
		int checkApplyNo = applyService.checkApply(apply);
		model.addAttribute("recruit", recruit);
		String applyMsg=null;
		int memberReputation = applyService.getMemberReputation(apply);
		int recruitReputation = applyService.getRecruitReputation(apply);
		System.out.println(memberReputation+"<----회원 내공");
		System.out.println(recruitReputation+"<----공고 내공");
		if(memberReputation < recruitReputation) {
			applyMsg="내공가 부족합니다.";
			System.out.println("내공 부족");
		}else {
			if(checkApplyNo==0) {
				applyService.addApply(apply);
				applyMsg="신청되었습니다.";
				System.out.println("신청가능");
			}
		}
		
		
		
		if(checkApplyNo==1) {
			applyMsg="이미 신청한 공고입니다.";
			System.out.println("이미신청");
		}
		
		String suggestType="";
		model.addAttribute("suggestType", suggestType);
		model.addAttribute("applyMsg", applyMsg);
		return "member/getRecruitInfo";
	}
	
}
