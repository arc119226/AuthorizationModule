package com.arcare.oauth.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arcare.oauth.dao.ClientInfoDAO;
import com.arcare.oauth.dao.DeveloperDAO;
import com.arcare.oauth.exception.AuthorizeException;
import com.arcare.oauth.po.ClientInfo;
import com.arcare.oauth.vo.AuthRequestVO;

/**
 * 
 * @author FUHSIANG_LIU /authorize/${version}/auth
 */
@Service
@Transactional
public class AuthService {

	@Autowired
	private DeveloperDAO developerDao;
	@Autowired
	private ClientInfoDAO clientInfoDao;

	public void test() {
		System.out.println("test start");
		System.out.println(this.developerDao.listAll().size());
		System.out.println("test end");
	}
	
	/**
	 * 驗證合法物件 不合法 直接中斷
	 * 
	 * @param version
	 * @param authReq
	 */
	public ClientInfo validate(AuthRequestVO authReq) {
		if (authReq == null) {// 沒有物件
			AuthorizeException error = new AuthorizeException();
			error.setError(AuthorizeException.INVALID_REQUEST);
			error.setError_description("request object is null.");
			throw error;
		}

		if (authReq.getRedirect_uri() == null) {// 未帶redirect_uri
			AuthorizeException error = new AuthorizeException();
			error.setError(AuthorizeException.INVALID_REQUEST);
			error.setError_description("redirect_uri is null.");
			throw error;
		}

		if (authReq.getClient_id() == null) {// 未帶client_id
			AuthorizeException error = new AuthorizeException();
			error.setError(AuthorizeException.UNAUTHORIZED_ClIENT);
			error.setError_description("client_id is null.");
			throw error;
		}

//		if (authReq.getScope() == null) {
//			AuthorizeException error = new AuthorizeException();
//			error.setError(AuthorizeException.INVALID_SCOPE);
//			error.setError_description("scope is null.");
//			throw error;
//		}

		if (authReq.getResponse_type() == null) {// 未帶response_type
			AuthorizeException error = new AuthorizeException();
			error.setError(AuthorizeException.UNSUPPORTED_GRANT_TYPE);
			error.setError_description("response_type is null.");
			throw error;
		}
		if (!authReq.getResponse_type().matches("code|token")) {// response_type 不合法
			AuthorizeException error = new AuthorizeException();
			error.setError(AuthorizeException.UNSUPPORTED_GRANT_TYPE);
			error.setError_description("response_type need code/token.");
			throw error;
		}

		Optional<ClientInfo> optClientInfo = this.clientInfoDao.findClientInfoByClientId(authReq.getClient_id(),"publish");
		if (optClientInfo.isPresent()) {
			ClientInfo clientInfo = optClientInfo.get();
			if (!authReq.getRedirect_uri().equals(clientInfo.getRedirectUri())) {
				AuthorizeException error = new AuthorizeException();
				error.setError(AuthorizeException.UNAUTHORIZED_ClIENT);
				error.setError_description("redirect_uri not vaild.");
				throw error;
			}
			System.out.println(clientInfo);
			return clientInfo;
			// validate scope
//			List<String> reqFunctions = Arrays.asList(authReq.getScope().split(","));
//			List<String> approveFunctions = Arrays.asList(clientInfo.getScope().split(","));
//			reqFunctions.forEach(reqFunction -> {
//				if (!approveFunctions.contains(reqFunction)) {
//					AuthorizeException error = new AuthorizeException();
//					error.setError(AuthorizeException.INVALID_SCOPE);
//					error.setError_description("scope not vaild.");
//					throw error;
//				}
//			});
		} else {
			AuthorizeException error = new AuthorizeException();
			error.setError(AuthorizeException.UNAUTHORIZED_ClIENT);
			error.setError_description("client not regist/apply.");
			throw error;
		}
	}

}
