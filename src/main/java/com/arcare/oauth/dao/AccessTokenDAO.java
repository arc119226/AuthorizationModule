package com.arcare.oauth.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.arcare.oauth.po.AccessToken;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@Repository
public class AccessTokenDAO extends BasicDao<AccessToken,Integer>{

	public Optional<AccessToken> findAccessTokenByRefreshToken(String refresh_token) {
		String hql = "FROM AccessToken C WHERE C.refreshToken = :refresh_token";
		Query<AccessToken> query = super.getSession().createQuery(hql,AccessToken.class);
		query.setParameter("refresh_token",refresh_token);
		List<AccessToken> list=query.list();
		return list.stream().findFirst();
	}
    
}
