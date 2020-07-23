package com.sbs.java.blog.controller;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sbs.java.blog.dto.CateItem;
import com.sbs.java.blog.dto.Member;
import com.sbs.java.blog.service.ArticleService;
import com.sbs.java.blog.service.MemberService;

public abstract class Controller {
	protected Connection dbConn;
	protected String actionMethodName;
	protected HttpServletRequest req;
	protected HttpServletResponse resp;
	protected HttpSession session;
	
	protected ArticleService articleService;
	protected MemberService memberService;
	
	public Controller(Connection dbConn, String actionMethodName, HttpServletRequest req, HttpServletResponse resp) {
		this.dbConn = dbConn;
		this.actionMethodName = actionMethodName;
		this.req = req;
		this.session = req.getSession();
		this.resp = resp;
		articleService = new ArticleService(dbConn);
		memberService = new MemberService(dbConn);
	}
	
	public abstract String getControllerName();
	
	public void beforeAction() {
		List<CateItem> cateItems = articleService.getForPrintCateItems();
		
		req.setAttribute("cateItems", cateItems);
		
		int loginedMemberId = -1;
		boolean isLogined = false;
		Member loginedMember = null;
		
		if(session.getAttribute("loginedMemberId") != null) {
			loginedMemberId = (int)session.getAttribute("loginedMemberId");
			isLogined = true;
			loginedMember = memberService.getMemberById(loginedMemberId);
		}
		
		req.setAttribute("loginedMemberId", loginedMemberId);
		req.setAttribute("isLogined", isLogined);
		req.setAttribute("loginedMember", loginedMember);
	}
	
	public void afterAction() {
		
	}
	
	public abstract String doAction();
	
	public String executeAction() {
		beforeAction();
		
		String doGuardRs = doGuard();
		
		if(doGuardRs != null) {
			return doGuardRs;
		}
		
	
		String rs = doAction();
		afterAction();
		
		return rs;
	}

	private String doGuard() {
		boolean isLogined = (boolean) req.getAttribute("isLogined");
		
		boolean needToLogin = false;
		
		String controllerName = getControllerName();
		
		switch(controllerName) {
		case "member":
			switch(actionMethodName) {
			case "doLogout":
				needToLogin = true;
				break;
			}
			break;
		case "article":
			switch(actionMethodName) {
			case "write":
			case "doWrite":
			case "modify":
			case "doModify":
			case "doDelete":
				needToLogin = true;
				break;
			}
			break;
		}
		
		if(needToLogin && isLogined == false) {
			return "html:<script> alert('로그인 후 이용해주세요.'); location.href = '../member/login'; </script>";
		}
		
		boolean needToLogout = false;
		
		switch(controllerName) {
		case "member":
			switch(actionMethodName) {
			case "login":
			case "join":
				needToLogout = true;
				break;
			}
			break;
		
		}
		
		if(needToLogout && isLogined) {
			return "html:<script> alert('로그아웃 후 이용해주세요.'); history.back(); </script>";
		}
		
		return null;
	}
}
