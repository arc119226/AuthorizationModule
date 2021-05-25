package com.arcare.oauth.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author ptx48
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberCenterResponseVO {

	@JsonProperty("status")
	private String status;// result status

	@JsonProperty("userType")
	private String userType;// admin,dev,etc...

	@JsonProperty
	private String email;

	@JsonProperty("msg")
	private String msg;// result message...

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
