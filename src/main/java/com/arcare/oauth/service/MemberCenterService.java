package com.arcare.oauth.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.arcare.oauth.vo.MemberCenterResponseVO;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@PropertySource("classpath:api.properties")
@Service
public class MemberCenterService {

	private final Logger logger = LoggerFactory.getLogger(MemberCenterService.class);
   
	@Autowired
	private Environment env;
	
	/**
    *
    * @param id
    * @return
    */
   public MemberCenterResponseVO checkMember(String username,String password){
       String apiName=env.getProperty("membercehter.url.check");
       logger.info(String.format("API {%s} BEGIN", apiName));
       long startTime = System.currentTimeMillis();
       try{

           StringBuilder sb=new StringBuilder(apiName);
           logger.info(String.format("API {%s} URL: %s", apiName, sb.toString()));

           if(null==username||null==password) {
        	   MemberCenterResponseVO member=new MemberCenterResponseVO();
        	   member.setStatus("ERROR");
    	       member.setMsg("登入失敗");
    	       return member;
           }else if("".equals(username.trim()) || "".equals(password.trim())) {
        	   MemberCenterResponseVO member=new MemberCenterResponseVO();
        	   member.setStatus("ERROR");
    	       member.setMsg("登入失敗");
    	       return member;
           }else {
               HttpHeaders headers = new HttpHeaders();
               headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
               MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
               map.add("username", username);
        	   map.add("password", password);
        	   
        	   HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        	   RestTemplate restTemplate = new RestTemplate();
        	   ResponseEntity<MemberCenterResponseVO> response = restTemplate.postForEntity( apiName, request , MemberCenterResponseVO.class );
        	   logger.info(String.format("API {%s} OUTPUT: %s", apiName, response.getBody()));	
        	   return response.getBody();
           }
       }catch(Exception e){
           logger.error(String.format("API {%s} EXCEPTION: %s", apiName, e.getMessage()));
           return null;
       }finally{
           logger.info(String.format("API {%s} END (%s ms)", apiName, (System.currentTimeMillis() - startTime)));
       }
   }
}
