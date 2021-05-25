package com.arcare.oauth.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.arcare.oauth.exception.TokenException;
import com.arcare.oauth.po.ClientInfo;
import com.arcare.oauth.po.Scope;
import com.arcare.oauth.service.AuthService;
import com.arcare.oauth.service.ClientInfoService;
import com.arcare.oauth.service.OAuthLoginService;
import com.arcare.oauth.vo.AuthRequestVO;
import com.arcare.oauth.vo.TokenPostVO;
import com.arcare.oauth.vo.UserInfoVO;
import com.google.gson.JsonParser;

/**
 * http://127.0.0.1:7080/ArcareAuthorizationModule/authorize/test
 * @author FUHSIANG_LIU
 *
 */
@Controller
@RequestMapping(value = "/authorize")
public class AuthorizeController {

	@Autowired
	private AuthService authService;

	@Autowired
	private OAuthLoginService loginService;

	@Autowired
	private ClientInfoService clientInfoService;

	@RequestMapping(value = "/test", produces="application/json")
	@ResponseBody
	public String index() {
		this.authService.test();
        return "{\"result\":\"OK\"}";
	}

	@RequestMapping(value = "/auth", method = { RequestMethod.GET })
	public String auth(Model model,
					   AuthRequestVO authReq,
					   HttpSession session,
					   HttpServletRequest request,
					   HttpServletResponse response) {

		 ClientInfo clientInfo=this.authService.validate(authReq);

		//通過驗證 將請求寫入session
		session.setAttribute("authReqObj", authReq);

		List<Scope> scopeList = clientInfoService.findAllScope()
			.stream()
			.filter(it-> it.getScopeValue().intValue() == (it.getScopeValue().intValue() &clientInfo.getScope().intValue()))
			.collect(Collectors.toList());

		model.addAttribute("scopeList", scopeList);

		return "oauth/login";
	}
	
	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	public ModelAndView login(UserInfoVO user,
							  HttpSession session,
							  HttpServletRequest request,
							  HttpServletResponse response) {
		if(loginService.validate(user)) {
			
			AuthRequestVO aur=AuthRequestVO.class.cast(session.getAttribute("authReqObj"));
			this.authService.validate(aur);
			
			if(aur.getResponse_type().equals("code")){
				//TODO authorization code 2/3
				System.out.println("authorization code 2/3");
				String grantCode = clientInfoService.generateGrantCodeByClientId(aur.getClient_id(), user.getUsername());
				if(null==grantCode || "".equals(grantCode)) {
					ModelAndView model=new ModelAndView("oauth/login");
					model.addObject("msg", "grant_code error");
					return model;
				}
				String redirectStr = String.format("%s?code=%s&state=%s", aur.getRedirect_uri(),grantCode,aur.getState());
				return new ModelAndView("redirect: "+redirectStr);
			}else if(aur.getResponse_type().equals("token")){
				//TODO Implicate 2/2
				String tokenJson=clientInfoService.useUsernamePasswordToChangeAccessToken(aur.getClient_id(), user.getUsername(), user.getPassword());
				String token=new JsonParser().parse(tokenJson).getAsJsonObject().get("access_token").getAsString();
				String redirectStr = String.format("%s#token=%s&state=%s", aur.getRedirect_uri(),token,aur.getState());
				return new ModelAndView("redirect: "+redirectStr);
			}

			ModelAndView model=new ModelAndView("oauth/login");
			model.addObject("msg", "response_type error");
			return model;
		}else {
			//return login page show error msg xxxx
			ModelAndView model=new ModelAndView("oauth/login");
			model.addObject("msg", "user not found");
			return model;
		}
	}
	@RequestMapping(value = "/token", method = {RequestMethod.POST})
	@ResponseBody
	public String token(TokenPostVO token,HttpServletRequest request,HttpServletResponse response) {
		//authorization code step 3/3
		//check grant_code can use if can use
		System.out.println(token);
		if("authorization_code".equals(token.getGrant_type())) {
			if(this.clientInfoService.checkValidGrantMode(token.getClient_id(),token.getCode())) {
				//generateToken
				String accessTokenJSON = this.clientInfoService.useGrantCodeToChangeAccessToken(token.getClient_id(), token.getUsername(), token.getCode());
				if(accessTokenJSON == null) {
					//throw error
					TokenException tokenExp=new TokenException();
					tokenExp.setError(TokenException.INVALID_GRANT);
					throw tokenExp;
				}
				//{"access_token":".....","token_type":"JWT","expires_in":3600,"refresh_token":"....."}
				return accessTokenJSON;
			}else {
				//throw error
				TokenException tokenExp=new TokenException();
				tokenExp.setError(TokenException.INVALID_CLIENT);
				throw tokenExp;
			}
		}
		
		if("password".equals(token.getGrant_type())) {
			//TODO password 1/1
			if(this.clientInfoService.checkValidPasswordMode(token.getClient_id(),token.getUsername(),token.getPassword())) {
				//check user validate
				//if valid
				String accessTokenJSON=this.clientInfoService.useUsernamePasswordToChangeAccessToken(token.getClient_id(), token.getUsername(), token.getPassword());
				if(accessTokenJSON==null) {
					//throw error
					TokenException tokenExp=new TokenException();
					tokenExp.setError(TokenException.INVALID_GRANT);
					throw tokenExp;
				}
				return accessTokenJSON;
			}else {
				//throw error
				TokenException tokenExp=new TokenException();
				tokenExp.setError(TokenException.INVALID_CLIENT);
				throw tokenExp;
			}
		}

		//client credentials 1/1
		if("client_credentials".equals(token.getGrant_type())) {
			//TODO　check client id and secret
			//generate token...
		}
		
		//refresh token 1/1 for authorization code		
		if("refresh_token".equals(token.getGrant_type())) {
			//regenerate token
			String refreshToken=clientInfoService.useRefreshTokenToGenerateNewAccessToken(token.getRefresh_token());
			if(refreshToken==null) {
				//throw error
				TokenException tokenExp=new TokenException();
				tokenExp.setError(TokenException.INVALID_REQUEST);
				throw tokenExp;
			}else {
				return refreshToken;
			}
		}
		//throw error
		TokenException tokenExp=new TokenException();
		tokenExp.setError(TokenException.INVALID_CLIENT);
		throw tokenExp;
	}
}
