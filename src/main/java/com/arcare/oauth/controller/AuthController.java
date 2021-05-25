package com.arcare.oauth.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.arcare.oauth.po.Developer;
import com.arcare.oauth.service.DeveloperService;
import com.arcare.oauth.service.MemberCenterService;
import com.arcare.oauth.vo.MemberCenterResponseVO;
/**
 * 
 * @author FUHSIANG_LIU
 *  後台登入
 */
@Controller
@RequestMapping(value = "/auth")
public class AuthController {

	@Autowired
	private MemberCenterService memberCenterService;

	@Autowired
	private DeveloperService developerService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model,HttpSession session) {
		session.setAttribute("user", null);
		return "redirect:/auth/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginAction(String username,String password,Model model,HttpSession session,HttpServletResponse response) {
		//deny refresh post
		response.setHeader("Pragma", "No-cache");
	    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    response.setDateHeader("Expires", 0);

		//post
		//query member center
		MemberCenterResponseVO result=memberCenterService.checkMember(username, password);
		if("ERROR".equals(result.getStatus())) {
			Map<String,String> attr=new HashMap<>();
			attr.put("msg", result.getMsg());
			model.addAllAttributes(attr);
			return "login";
		}else {
			//if have user
			//	check developer table username
			// if didn't have developer
			//		add developer data
			// if developer exists
			//		redirect to developer stage
			// add developer to session[user]
			Developer developer=developerService.createOrUpdateDeveloper(username,result);
			session.setAttribute("user", developer);

			return "redirect:/index";
		}
	}
}
