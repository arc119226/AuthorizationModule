package com.arcare.oauth.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arcare.oauth.dao.DeveloperDAO;
import com.arcare.oauth.po.Developer;
import com.arcare.oauth.util.Page;
import com.arcare.oauth.vo.MemberCenterResponseVO;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@Service
@Transactional
public class DeveloperService {

	@Autowired
	private DeveloperDAO developerDao;

	/**
	 * 
	 * @param username
	 * @param member
	 */
	public Developer createOrUpdateDeveloper(String username,MemberCenterResponseVO member) {
		List<Developer> developers=this.findByUsername(username);
		if(developers.stream().findFirst().isPresent()) {
			Developer dev=developers.stream().findFirst().get();
			dev.setEmail(member.getEmail());
			dev.setLastUpdated(new Date());
			dev.setUserType(member.getUserType());
			this.saveOrUpdate(dev);
			return dev;
		}else {
			Developer dev=new Developer();
			dev.setUsername(username);
			dev.setPassword("");//save in membere center
			dev.setEmail(member.getEmail());
			dev.setDateCreated(new Date());
			dev.setLastUpdated(new Date());
			dev.setUserType(member.getUserType());
			this.saveOrUpdate(dev);
			return dev;
		}
	}
	
	public Page<Developer> findPagne(Integer pageNo,Integer pageSize){
		if(pageNo==null) {
			pageNo=1;
		}
		if(pageSize==null) {
			pageSize=10;
		}
		Page<Developer> results=developerDao.pageQuery(developerDao.createCriteria(), pageNo, pageSize);
		results.getResult().forEach(developer->{
			developer.getClientInfos().forEach(client->{
				
			});
		});
		return results;
	}
	
	public List<Developer> findByUsername(String username) {
		return developerDao.findByProperty("username", username);
	}
	
	/**
	 * 依據id取得物件
	 * @param id
	 * @return
	 */
	public Developer findById(Integer id) {
		return developerDao.findById(id);
	}
	/**
	 * 取得所有資料列表
	 * @return
	 */
	public List<Developer> findAll() {
		return developerDao.listAll();
	}
	
	/**
	 * 存檔或新增一筆
	 * @param obj
	 */
	public void saveOrUpdate(Developer obj) {
		developerDao.getSession().saveOrUpdate(obj);

	}
	/**
	 * 依據id進行刪除
	 * @param id
	 */
	public void delete(int id) {
		developerDao.deleteById(id);
	}
}
