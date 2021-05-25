package com.arcare.oauth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.arcare.oauth.util.HashUtil;
import com.google.gson.JsonParser;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@Controller
@RequestMapping(value = "/clientService")
@PropertySource("classpath:api.properties")
public class FakeClientService {

	private final Logger logger = LoggerFactory.getLogger(FakeClientService.class);
	@Autowired
	private Environment env;

	/**
	 * login get grant code
	 * 
	 * @param model
	 * @param code
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/webServer", method = RequestMethod.GET)
	public String event01(Model model, String code) {
		model.addAttribute("code", code);
		return "event/grantCode";
	}

	/**
	 * post grant code
	 * 
	 * @param model
	 * @param code
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/webServer", method = RequestMethod.POST)
	public String event01Action(Model model, String code) {

		// 呼叫認證中心 用code 申請 token
		System.out.println("grant_code:"+code);
		String tokenJson=this.getTokenByGrantCode("authorization_code", 
					code,
					"http://127.0.0.1:8080/ArcareAuthorizationModule/clientService/webServer", 
					env.getProperty("client.id"));
		
		if(tokenJson!=null) {
			model.addAttribute("hasToken", "true");
			//TODO get resource
			String token=new JsonParser().parse(tokenJson).getAsJsonObject().get("access_token").getAsString();
			System.out.println("use token to get resource");
		}
		System.out.println(tokenJson);
		return "event/grantCode";
	}
	
	public static void main(String args[]) {
		String json="{\"access_token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGllbnRfaWQiOiI0NTc0OTg4My1jMzcxLTRlNzYtYTY0Yy0zMzE4ZDNlZTc2NDAiLCJkZXZlbG9wZXJfdXNlcm5hbWUiOiJhcmMiLCJ1c2VybmFtZSI6Im51bGwiLCJkYXRlX2NyZWF0ZWQiOiIyMDE4LTA5LTIwIDE2OjQ0OjAzLjAiLCJleHBpcmVfZGF0ZSI6Ik1vbiBTZXAgMjQgMTY6MDg6MTkgQ1NUIDIwMTgiLCJzY29wZSI6IjYyIn0=.KilIEKjr21730VOFlW7UFDBzenJsfZ/5Sz0o504nkO4=\",\"token_type\":\"JWT\",\"expires_in\":600000,\"refresh_token\":\"8ddf58be-0eb2-4fa9-9dde-bfbfbf115be4\"}\r\n" + 
				"";

		System.out.println(new JsonParser().parse(json).getAsJsonObject().get("access_token").getAsString());
	}
	
	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public String event02(Model model) {
		return "event/password";
	}
	
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public String event02Action(Model model,String useranme,String password) {
		return "event/password";
	}

	private String getTokenByGrantCode(String grant_type, String code, String redirect_uri, String client_id) {
		String apiName = env.getProperty("token.url");
		System.out.println(apiName);
		logger.info(String.format("API {%s} BEGIN", apiName));
		long startTime = System.currentTimeMillis();
		try {

			StringBuilder sb = new StringBuilder(apiName);
			logger.info(String.format("API {%s} URL: %s", apiName, sb.toString()));

			HttpHeaders headers = new HttpHeaders();
			System.out.println(
					"Basic " + HashUtil.encodeBasic(env.getProperty("client.id"), env.getProperty("client.secret")));
			headers.set("Authorization",
					"Basic " + HashUtil.encodeBasic(env.getProperty("client.id"), env.getProperty("client.secret")));
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("grant_type", grant_type);
			map.add("code", code);
			map.add("redirect_uri", redirect_uri);
			map.add("client_id", client_id);

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
					headers);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.postForEntity(apiName, request, String.class);
			logger.info(String.format("API {%s} OUTPUT: %s", apiName, response.getBody()));
			return response.getBody();
		} catch (Exception e) {
			logger.error(String.format("API {%s} EXCEPTION: %s", apiName, e.getMessage()));
			return null;
		} finally {
			logger.info(String.format("API {%s} END (%s ms)", apiName, (System.currentTimeMillis() - startTime)));
		}
	}

}
