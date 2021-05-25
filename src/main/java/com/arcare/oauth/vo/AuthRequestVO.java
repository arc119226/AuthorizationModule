package com.arcare.oauth.vo;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
public class AuthRequestVO {

	private String response_type;

	private String client_id;

	private String redirect_uri;
	
	private String scope="";//function_name

	private String state="";//state

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "AuthRequest [response_type=" + response_type + ", client_id=" + client_id + ", redirect_uri="
				+ redirect_uri + ", scope=" + scope + ", state=" + state + "]";
	}
}
