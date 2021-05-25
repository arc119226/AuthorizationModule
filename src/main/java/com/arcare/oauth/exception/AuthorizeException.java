package com.arcare.oauth.exception;
/**
 * 
 * @author FUHSIANG_LIU
 * Authorize ERROR
 */
public class AuthorizeException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * invalid_request
	 */
	public static final String INVALID_REQUEST = "invalid_request";
	/**
	 * invalid_client
	 */
	public static final String INVALID_CLIENT = "invalid_client";
	/**
	 * invalid_grant
	 */
	public static final String INVALID_GRANT = "invalid_grant";
	/**
	 * unauthorized_client
	 */
	public static final String UNAUTHORIZED_ClIENT = "unauthorized_client";
	/**
	 * unsupported_grant_type
	 */
	public static final String UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
	/**
	 * invalid_scope
	 */
	public static final String INVALID_SCOPE = "invalid_scope";
	
	/**
	 * error msg
	 */
	private String error;
	/**
	 * error description
	 */
	private String error_description;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

}
