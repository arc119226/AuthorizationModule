package com.arcare.oauth.exception;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
public class UserAuthException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * error msg
	 */
	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
