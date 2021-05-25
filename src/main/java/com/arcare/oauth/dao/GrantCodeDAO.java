package com.arcare.oauth.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.arcare.oauth.po.GrantCode;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@Repository
public class GrantCodeDAO extends BasicDao<GrantCode,Integer>{

	/**
	 * 
	 * @param grantCode
	 * @return
	 */
	public Optional<GrantCode> findGrantCodeByGrantCode(String grantCode) {
		String hql = "FROM GrantCode g WHERE g.grantCode = :grantCode";
		Query<GrantCode> query = super.getSession().createQuery(hql,GrantCode.class);
		query.setParameter("grantCode" , grantCode);
		List<GrantCode> list=query.list();
		return list.stream().findFirst();
	}

}
