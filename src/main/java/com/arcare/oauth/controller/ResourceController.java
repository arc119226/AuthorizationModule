package com.arcare.oauth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@Controller
@RequestMapping(value = "/resource")
public class ResourceController {

	@RequestMapping(value = "${version}/${function}", method = {RequestMethod.POST})
	public String token(@PathVariable("version") String version,
						@PathVariable("function") String function,
						HttpServletRequest request, 
						HttpServletResponse response) {

		//TODO define function service

		return null;
	}
}
