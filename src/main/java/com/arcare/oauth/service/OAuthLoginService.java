package com.arcare.oauth.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arcare.oauth.vo.MemberCenterResponseVO;
import com.arcare.oauth.vo.UserInfoVO;
/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@Service
@Transactional
public class OAuthLoginService {
	
	@Autowired
	private MemberCenterService memberCenterService;
	
	public Boolean validate(UserInfoVO userinfo) {
		if(userinfo ==null || userinfo.getUsername()==null || userinfo.getPassword()==null) {
			return false;
		}
		MemberCenterResponseVO member=memberCenterService.checkMember(userinfo.getUsername(), userinfo.getUsername());
		if("ERROR".equals(member.getStatus())) {
			return false;
		}else {
			return true;
		}
	}
}
