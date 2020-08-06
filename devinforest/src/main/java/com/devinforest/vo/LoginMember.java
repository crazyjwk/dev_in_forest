package com.devinforest.vo;

import javax.servlet.http.HttpSession;

public class LoginMember {
	private String memberEmail;
	private String memberName;
	private String memberPassword;
	private String accountKind;
	private int memberReputation;
	
	public boolean getLoginSession(HttpSession session) {
		return session.getAttribute("loginMember") != null;
	}
	
	public String getMemberEmail() {
		return memberEmail;
	}
	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberPassword() {
		return memberPassword;
	}
	public void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}
	public String getAccountKind() {
		return accountKind;
	}
	public void setAccountKind(String accountKind) {
		this.accountKind = accountKind;
	}
	public int getMemberReputation() {
		return memberReputation;
	}
	public void setMemberReputation(int memberReputation) {
		this.memberReputation = memberReputation;
	}
	@Override
	public String toString() {
		return "LoginMember [memberEmail=" + memberEmail + ", memberName=" + memberName + ", memberPassword="
				+ memberPassword + ", accountKind=" + accountKind + ", memberReputation=" + memberReputation + "]";
	}
	
}
