package com.arcare.oauth.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class HashUtil {

	/**
	 * http basic auth
	 * @param clientId
	 * @param clientSecret
	 * @return
	 */
	public static String encodeBasic(String clientId,String clientSecret) {
		String data=clientId+":"+clientSecret;
		try {
			String asB64 = Base64.getEncoder().encodeToString(data.getBytes("utf-8"));
			return asB64;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String encodeBase64(String str) {
		String data=str;
		try {
			String asB64 = Base64.getEncoder().encodeToString(data.getBytes("utf-8"));
			return asB64;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decodeBase64(String asB64) {
		byte[] asBytes = Base64.getDecoder().decode(asB64);
		try {
			return new String(asBytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	//secret -> save in server
	//HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), "secret")
	/**
	 * jwt token generater
	 * @param header
	 * @param payload
	 * @param secret
	 * @return
	 */
	public static String jWTHmacSHA256VSign(String header,String payload,String secret) {  
        byte[] dataByte;
		try {
			SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
		    Mac mac = Mac.getInstance("HmacSHA256");
		    mac.init(key);
		    String bHeader = Base64.getEncoder().encodeToString(header.getBytes("utf-8"));
		    String bPayload = Base64.getEncoder().encodeToString(payload.getBytes("utf-8"));
		    String data = bHeader+"."+bPayload;
			dataByte = data.getBytes("UTF-8");
			String vSign = Base64.getEncoder().encodeToString(mac.doFinal(dataByte));
			return vSign;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
	}
	/**
	 * varify access token
	 * @param token
	 * @param secret
	 * @return
	 */
	public static boolean isValidToken(String token,String secret) {
		try {
			String[] datas=token.split("\\.");
			String header=HashUtil.decodeBase64(datas[0]);
			String payload=HashUtil.decodeBase64(datas[1]);
			String sign=datas[2];
			String validateSign = jWTHmacSHA256VSign(header, payload, secret);
			return validateSign.equals(sign);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	public static void main(String args[]) {
//		//test encode http basic auth
//		String basicHeader=HashUtil.encodeBasic(client_id, client_secret);
//		System.out.println(basicHeader);
//		//test decode base64 http basic auth
//		String headerDecode=HashUtil.decodeBase64(basicHeader);
//		System.out.println(headerDecode);
//		
//		String jwtHeader64="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
//		String jwtBody64="eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9";
//		String jwtHeader=HashUtil.decodeBase64(jwtHeader64);
//		System.out.println(jwtHeader);
//		String jwtBody=HashUtil.decodeBase64(jwtBody64);
//		System.out.println(jwtBody);
//
//		//server sign token
//		String vsign=HashUtil.jWTHmacSHA256VSign(jwtHeader, jwtBody, "secret111");
//		String token=jwtHeader64+"."+jwtBody64+"."+vsign;
//		System.out.println(token);
//		System.out.println(HashUtil.isValidToken(token, "secret111"));
//	}
}
