package com.arcare.oauth.vo;
/**
 * 
 * @author FUHSIANG_LIU
 *
 */
public class TokenPostVO {

	//authorization_code
	//password
	//client_credentials
	private String grant_type;
	
	//authorization_code -> grant_code
	private String code;
	
	//authorization_code -> callback uri
	private String redirect_uri;
	
	//authorization_code -> client_id
	private String client_id;
	
	//password
	private String username;
	
	//password
	private String password;
	
	//refresh token
	private String refresh_token;

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

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

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
}
