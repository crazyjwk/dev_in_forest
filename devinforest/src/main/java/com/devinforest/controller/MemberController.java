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

import com.devinforest.mapper.MemberMapper;
import com.devinforest.service.CompanyService;
import com.devinforest.service.MemberService;
import com.devinforest.service.SuggestService;
import com.devinforest.vo.LoginAdmin;
import com.devinforest.vo.LoginCompany;
import com.devinforest.vo.LoginMember;
import com.devinforest.vo.Member;
import com.devinforest.vo.Restoration;
import com.devinforest.vo.Suggest;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private SuggestService suggestService;
	
	//회원가입 및 로그인선택페이지
	   @GetMapping("/login")
	   public String login(HttpSession session){
	      if(session.getAttribute("loginMember")!=null) {
	         return "index/home";
	      }
	      if(session.getAttribute("loginCompany")!=null) {
	         return "company/companyHome";
	      }
	      if(session.getAttribute("loginAdmin")!=null) {
	         return "redirect:/adminHome";
	      }
	      return "index/login";
	   }
	//회원 재가입 요청
	@GetMapping("/requestMemberRestore")
	public String requestMemberRestore(HttpSession session) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/index";
		}
		return "member/requestMemberRestore";
	}
	@PostMapping("/requestMemberRestore")
	public String requestMemberRestore(HttpSession session, Restoration restoration) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/index";
		}
		memberService.addRequestRestoreMember(restoration);
		System.out.println(restoration+"<---post controller restoration");
		return "index/index";
	}
	//회원비밀번호 찾기
	@GetMapping("/findMemberPw")
	public String findMemberPw(HttpSession session) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/index";
		}
		return "member/findMemberPw";
	}
	@PostMapping("/findMemberPw")
	public String findMemberPw(HttpSession session,LoginMember loginMember, Model model) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/index";
		}
		memberService.findMemberPw(loginMember);
		model.addAttribute("msg","변경된 비밀번호를 이메일로 보냈습니다.");
		return "member/findMemberPw";
	}
	//회원이메일 찾기
	@GetMapping("/findMemberEmail")
	public String findMemberEmail(HttpSession session,LoginMember loginMember) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/index";
		}
		return "member/findMemberEmail";
	}
	//회원이메일 찾기 액션
	@PostMapping("/findMemberEmail")
	public String findMemberEmail(HttpSession session,Model model,Member member) {
		
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/index";
		}
		String findEmail = memberService.findMemberEmail(member);
		if(findEmail == null) {
			model.addAttribute("msg","이메일을 찾을 수 없습니다.");
		}else {
			model.addAttribute("msg",findEmail);
		}
		return "member/findMemberEmail";
	}
	//회원목록
		@GetMapping("/getMemberList")
		public String getMemberList(HttpSession session, Model model,
				   @RequestParam(defaultValue = "1") int currentPage,
				   @RequestParam(defaultValue = "5") int rowPerPage,
				   @RequestParam(defaultValue = "") String searchWord) {
			if(session.getAttribute("loginMember")==null) {
				return "redirect:/index";
			}
			Map<String, Object> map = memberService.getMemberList(currentPage, rowPerPage, searchWord);
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("rowPerPage", rowPerPage);
			model.addAttribute("searchWord", searchWord);
			model.addAttribute("memberTotalCount", map.get("memberTotalCount"));
			model.addAttribute("lastPage", map.get("lastPage"));
			model.addAttribute("memberList", map.get("memberList"));
			return "member/memberList";
		}
		//기업용 회원목록
        @GetMapping("/getMemberListForCompany")
        public String getMemberListForCompany(HttpSession session, Model model,
                 @RequestParam(defaultValue = "1") int currentPage,
                 @RequestParam(defaultValue = "5") int rowPerPage,
                 @RequestParam(defaultValue = "") String searchWord) {
           if(session.getAttribute("loginCompany")==null) {
              return "redirect:/index";
           }
           Map<String, Object> map = memberService.getMemberList(currentPage, rowPerPage, searchWord);
           model.addAttribute("currentPage", currentPage);
           model.addAttribute("rowPerPage", rowPerPage);
           model.addAttribute("searchWord", searchWord);
           model.addAttribute("memberTotalCount", map.get("memberTotalCount"));
           model.addAttribute("lastPage", map.get("lastPage"));
           model.addAttribute("memberList", map.get("memberList"));
           return "company/memberListForCompany";
        }
	//회원탈퇴
	@GetMapping("/removeMember")
	public String removeMember(HttpSession session,Model model,LoginMember loginMember) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/index";
		}
		Member member = memberService.getMemberInfo(loginMember);
		model.addAttribute("member", member);
		return "member/removeMember";
	}
	@PostMapping("/removeMember")
	public String removeMember(HttpSession session,LoginMember loginMember) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		memberService.removeMember(loginMember);
		session.invalidate();
		return "redirect:/index";
	}
	//비밀번호 일치하면 수정폼으로 이동
	//회원정보 수정
	@GetMapping("/modifyMember")
	public String modifyMember(HttpSession session) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/index";
		}
		return "member/memberPwCheck";
	}
	@GetMapping("/memberPwCheck")
	public String memberPwCheck(HttpSession session,LoginMember loginMember,Model model) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/index";
		}
		LoginMember returnLoginMember = memberService.memberLogin(loginMember);
		
		System.out.println("loginMember-->"+loginMember);
		System.out.println("returnloginMember-->"+returnLoginMember);
		if(returnLoginMember == null) {
			System.out.println("비밀번호 인증실패");
			return "index/home";
		}else {
			session.setAttribute("loginMember", returnLoginMember);
			System.out.println("로그인성공");
			Member member = new Member();
			member=memberService.getMemberInfo(loginMember);
			member.setMemberPassword(loginMember.getMemberPassword());
			model.addAttribute("member",member);
			return "member/modifyMember";
		}
	}
	@PostMapping("/modifyMember")
	public String modifyMember(HttpSession session,Member member,Model model) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/index";
		}
		memberService.modifyMember(member);
		Member modifiedMember = new Member();
		LoginMember loginMember = new LoginMember();
		loginMember.setMemberEmail(member.getMemberEmail());
		loginMember.setMemberName(member.getMemberName());
		member=memberService.getMemberInfo(loginMember);
		model.addAttribute("member",member);
		List<Suggest> suggestList = suggestService.getSuggestList(member.getMemberName());
		System.out.println(suggestList+"<---memberInfo");
		model.addAttribute("suggestList", suggestList);
		return "member/memberInfo";
	}
	//회원정보 상세보기
	@GetMapping("/getMemberInfo")
	public String getMemberInfo(HttpSession session, Model model, LoginMember loginMember) {
		Member member = new Member();
		member=memberService.getMemberInfo(loginMember);
		System.out.println(member+"<---member");		
		model.addAttribute("member",member);
		if(session.getAttribute("loginMember")!=null) {
			List<Suggest> suggestList = suggestService.getSuggestList(member.getMemberName());
			System.out.println(suggestList+"<---memberInfo");
			model.addAttribute("suggestList", suggestList);
			return "member/memberInfo";
		}
		if(session.getAttribute("loginCompany")!=null) {
			return "company/getMemberInfoByCompany";
		}
		
		
		return "redirect:/index";
	}
	
	//회원가입 폼으로 이동
	@GetMapping("/addMember")
	public String addMember(HttpSession session, Model model) {
		//로그인 중일때
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/index";
		}
		LoginMember loginMember = new LoginMember();
		model.addAttribute("NameMsg", "");
		model.addAttribute("EmailMsg", "");
		model.addAttribute("loginMember", loginMember);
		return "member/addMember";
	}
	//회원가입 액션
	@PostMapping("/addMember")
	public String addMember(LoginMember loginMember, HttpSession session, Model model) {
		//로그인 중일때
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/index";
		}
		memberService.addMember(loginMember);
		model.addAttribute("loginMember", loginMember);
		return "redirect:/index";
		
	}
	//로그인 폼으로 이동
	@GetMapping("/memberLogin")
	public String memberLogin(HttpSession session) {
		//로그인 중일때
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/index";
		}
		
		return "member/memberLogin";
	}
	//로그인 액션
	@PostMapping("/memberLogin")
	public String memberLogin(HttpSession session,LoginMember loginMember) {
		//로그인 중일때
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/index";
		}
		LoginMember returnLoginMember = memberService.memberLogin(loginMember);
		
		System.out.println("loginMember-->"+loginMember);
		System.out.println("returnloginMember-->"+returnLoginMember);
		if(returnLoginMember == null) {
			System.out.println("로그인실패");
			//회원로그인 실패시 관리자 서비스 로그인 시도 
			return "member/memberLogin";
		}
		returnLoginMember.setMemberPassword("");//비밀번호 지워주기!!
		//로그인 회원 종류 구별
		//일반회원
		if(returnLoginMember.getAccountKind().equals("M")) {
			
			Member member = memberService.getMemberInfo(returnLoginMember);
			returnLoginMember.setMemberReputation(member.getMemberReputation());
			System.out.println(returnLoginMember+"<-------로그인 컨트롤러 액션");	
			session.setAttribute("loginMember", returnLoginMember);
			System.out.println("로그인성공");
			return "index/home";
			
			
		}
		if(returnLoginMember.getAccountKind().equals("A")) {
			System.out.println(returnLoginMember+" <- 로그인 컨트롤러 액션(관리자)");
			LoginAdmin loginAdmin = new LoginAdmin();
			loginAdmin.setAdminEmail(returnLoginMember.getMemberEmail());
			loginAdmin.setAdminName(returnLoginMember.getMemberName());
			loginAdmin.setAccountKind(returnLoginMember.getAccountKind());
			System.out.println(loginAdmin+" <- 관리자 주입 확인");
			session.setAttribute("loginAdmin", loginAdmin);
			System.out.println("로그인성공");
			return "redirect:/adminHome";
		}
		if(returnLoginMember.getAccountKind().equals("C")) {
			
			System.out.println(returnLoginMember+"<-------로그인 컨트롤러 액션(기업)");	
			LoginCompany returnLoginCompany = new LoginCompany();
			returnLoginCompany.setCompanyEmail(returnLoginMember.getMemberEmail());
			returnLoginCompany = companyService.companyLogin(returnLoginCompany);
			System.out.println(returnLoginCompany+"<-------로그인 컨트롤러 액션");	
			session.setAttribute("loginCompany", returnLoginCompany);
			System.out.println("로그인성공");
			return "company/companyHome";
		}
		else {
			return "redirect:/index";
		}
	}
	//로그인 아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		if(session.getAttribute("loginMember")==null && session.getAttribute("loginAdmin")==null && session.getAttribute("loginCompany")==null) {
	         return "redirect:/index";
	      }
		session.invalidate();
		return "redirect:/index";
	}
	
}
