package com.arcare.oauth.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberCenterRequestVO {
	/**
	 * 帳號
	 */
	@JsonProperty("username")
	private String username;
	/**
	 * 密碼
	 */
	@JsonProperty("password")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "MemberCenterRequestVO [username=" + username + ", password=" + password + "]";
	}



}
