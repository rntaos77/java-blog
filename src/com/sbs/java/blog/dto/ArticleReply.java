package com.sbs.java.blog.dto;

import java.util.Map;

public class ArticleReply extends Dto{
	private int articleId;
	
	private int memberId;
	private String body;
	
	public ArticleReply(Map<String, Object> row) {
		super(row);
		
		this.articleId = (int) row.get("articleId");
		this.memberId = (int) row.get("memberId");
		this.body = (String) row.get("body");
		
	}

	public String getBodyForXTemplate() {
		return body.replaceAll("(?i)script", "<!--REPLACE:script-->");
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "ArticleReply [articleId=" + articleId + ", memberId=" + memberId + ", body=" + body + ", toString()="
				+ super.toString() + ", getExtra()=" + getExtra() + ", getId()=" + getId() + ", getRegDate()="
				+ getRegDate() + "]";
	}
	
	
}
