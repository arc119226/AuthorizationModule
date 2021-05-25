package com.arcare.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arcare.oauth.vo.MemberCenterRequestVO;
import com.arcare.oauth.vo.MemberCenterResponseVO;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@Controller
@RequestMapping(value = "/membercenter")
public class FakeMemberCenterController {
	/**
	 * fake member center login
	 * @param postObj
	 * @return
	 */
    @RequestMapping(value="/login.json",method = RequestMethod.POST, produces="application/json;charset=utf-8")
    public @ResponseBody MemberCenterResponseVO validateMember(MemberCenterRequestVO postObj) {
    	System.out.println(postObj);
    	MemberCenterResponseVO member=new MemberCenterResponseVO();
        if(null==postObj.getUsername()||null==postObj.getPassword()) {
     	   member.setStatus("ERROR");
 	       member.setMsg("login fail");
        }else if("".equals(postObj.getUsername().trim())||"".equals(postObj.getUsername().trim())){
      	   member.setStatus("ERROR");
  	       member.setMsg("login fail");
        }else {
     	   if(postObj.getUsername().equals("arc")) {
	    	       member.setStatus("OK");
	    	       member.setEmail("ptx48691@gmail.com");
	    	       member.setUserType("admin");
	    	       member.setMsg("admin login success");
     	   }else {
	    	       member.setStatus("OK");
	    	       member.setEmail("xxxx@gmail.com");
	    	       member.setUserType("dev");
	    	       member.setMsg("developer login success");
     	   }
        }
    	return member;
    }

}
