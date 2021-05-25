package com.arcare.oauth.interceptor;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.arcare.oauth.exception.AuthorizeException;
import com.arcare.oauth.po.ClientInfo;
import com.arcare.oauth.service.ClientInfoService;
import com.arcare.oauth.util.HashUtil;

@Component
public class TokenInterceptor implements HandlerInterceptor {
	
//	@Autowired
//	private ClientInfoDAO clientInfoDAO;
	@Autowired
	private ClientInfoService clientInfoService;
	
	//validate header
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
		String basicAuth64 = request.getHeader("Authorization");
		System.out.println(basicAuth64);
//		Authorization: Basic base64(client_id:client_secret)
		String basic =basicAuth64.split(" ")[0];
		System.out.println(basic);
		String base64 = basicAuth64.split(" ")[1];
		System.out.println(base64);
		String basicAuth = HashUtil.decodeBase64(base64);
		System.out.println(basicAuth);
		String clientId = basicAuth.split(":")[0];
		System.out.println(clientId);
		String clientSecret = basicAuth.split(":")[1];
		System.out.println(clientSecret);
		Optional<ClientInfo> optClientInfo = clientInfoService.findByClientIdAndClientSecret(clientId, clientSecret, "publish");
		System.out.println("---token before---");
		if(optClientInfo.isPresent()) {
			request.setAttribute("clientId", clientId);
			request.setAttribute("clientSecret", clientSecret);
			System.out.println("client is valid");
			return true;
		}else {
			AuthorizeException ex=new AuthorizeException();
			ex.setError(AuthorizeException.INVALID_CLIENT);
			return false;
		}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("---token executed---");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("---token Completed---");
	}

}
