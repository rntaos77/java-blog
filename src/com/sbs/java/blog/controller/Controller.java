package com.sbs.java.blog.controller;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sbs.java.blog.dto.CateItem;
import com.sbs.java.blog.service.ArticleService;

public abstract class Controller {
	protected Connection dbConn;
	protected String actionMethodName;
	protected HttpServletRequest req;
	protected HttpServletResponse resp;
	
	protected ArticleService articleService;
	
	public Controller(Connection dbConn, String actionMethodName, HttpServletRequest req, HttpServletResponse resp) {
		this.dbConn = dbConn;
		this.actionMethodName = actionMethodName;
		this.req = req;
		this.resp = resp;
		articleService = new ArticleService(dbConn);
	}
	
	public void beforeAction() {
		List<CateItem> cateItems = articleService.getForPrintCateItems();
		
		req.setAttribute("cateItems", cateItems);
	}
	
	public void afterAction() {
		
	}
	
	public abstract String doAction();
	
	public String executeAction() {
		beforeAction();
		String rs = doAction();
		afterAction();
		
		return rs;
	}
}
