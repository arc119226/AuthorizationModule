package com.arcare.oauth.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.arcare.oauth.po.ClientInfo;
/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@Repository
public class ClientInfoDAO extends BasicDao<ClientInfo,Integer>{

	/**
	 * 依據client_id client_state 取得ClientInfo
	 * @param clientId
	 * @return
	 */
	public Optional<ClientInfo> findClientInfoByClientId(String clientId,String clientState) {
		String hql = "FROM ClientInfo C WHERE C.clientId = :client_id and clientState = :client_state";
		Query<ClientInfo> query = super.getSession().createQuery(hql,ClientInfo.class);
		query.setParameter("client_id",clientId);
		query.setParameter("client_state",clientState);
		List<ClientInfo> list=query.list();
		return list.stream().findFirst();
	}
	/**
	 * 依據client_id client_secret client_state
	 * @param clientId
	 * @param clientSecret
	 * @param clientState
	 * @return
	 */
	public Optional<ClientInfo> findByClientIdAndClientSecret(String clientId,String clientSecret,String clientState) {
		String hql = "FROM ClientInfo c WHERE c.clientId = :client_id and c.clientSecret = :client_secret and c.clientState = :client_state";
		Query<ClientInfo> query = super.getSession().createQuery(hql,ClientInfo.class);
		query.setParameter("client_id",clientId);
		query.setParameter("client_secret",clientSecret);
		query.setParameter("client_state",clientState);
		List<ClientInfo> list=query.list();
		return list.stream().findFirst();
	}
}
