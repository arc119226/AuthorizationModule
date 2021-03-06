package com.arcare.oauth.service;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arcare.oauth.dao.AccessTokenDAO;
import com.arcare.oauth.dao.ClientInfoDAO;
import com.arcare.oauth.dao.ClientInfoDetailDAO;
import com.arcare.oauth.dao.ClientTypeDAO;
import com.arcare.oauth.dao.GrantCodeDAO;
import com.arcare.oauth.dao.ScopeDAO;
import com.arcare.oauth.po.AccessToken;
import com.arcare.oauth.po.ClientInfo;
import com.arcare.oauth.po.ClientInfoDetail;
import com.arcare.oauth.po.ClientType;
import com.arcare.oauth.po.Developer;
import com.arcare.oauth.po.GrantCode;
import com.arcare.oauth.po.Scope;
import com.arcare.oauth.util.HashUtil;
import com.arcare.oauth.util.Page;
import com.arcare.oauth.vo.UserInfoVO;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@Service
@Transactional
public class ClientInfoService {

	@Autowired
	private ClientInfoDAO clientInfoDAO;

	@Autowired
	private ClientInfoDetailDAO clientInfoDetailDAO;

	@Autowired
	private ClientTypeDAO clientTypeDAO;

	@Autowired
	private ScopeDAO scopeDAO;

	@Autowired
	private GrantCodeDAO grantCodeDAO;

	@Autowired
	private AccessTokenDAO accessTokenDAO;

	@Autowired
	private OAuthLoginService oAuthLoginService;


	
	public String useRefreshTokenToGenerateNewAccessToken(String refreshToken) {
		Optional<AccessToken> optToken=accessTokenDAO.findAccessTokenByRefreshToken(refreshToken);
		if(optToken.isPresent()) {
			AccessToken token=optToken.get();
			if(!token.getClientInfo().getClientState().equals("publish")) {
				return null;//not allow
			}
			if(token.getTokenState()!=0) {
				return null;//not allow
			}
			
			ClientInfo clientInfo = token.getClientInfo();
			Optional<ClientInfoDetail> optClientInfoDetail = clientInfo.getClientInfoDetails().stream().findFirst();
			ClientInfoDetail clientInfoDetail = optClientInfoDetail.get();

			String signKey = clientInfoDetail.getSignKey();
			String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

			final long ONE_MINUTE_IN_MILLIS = 600000;// millisecs 60??????
			Calendar date = Calendar.getInstance();
			long t = date.getTimeInMillis();
			Date expireDate = new Date(t + (10 * ONE_MINUTE_IN_MILLIS));

			// client_id , developer_username , username , date_created , expire_date ,
			// scope
			String payloadTemplate = "{\"client_id\":\"%s\",\"developer_username\":\"%s\",\"username\":\"%s\",\"date_created\":\"%s\",\"expire_date\":\"%s\",\"scope\":\"%s\"}";
			String payload = String.format(payloadTemplate, clientInfo.getClientId() , clientInfo.getDeveloper().getUsername(), token.getUsername(),
					clientInfo.getDateCreated(), expireDate, clientInfo.getScope());
			// TODO use grant code to change access token
			// sign=HMACSHA256(base64(header)+'.'+base64(payload)+'.'+sign_key)
			// Access_token=base64(header)+'.'+base64(payload)+'.'+sign
			String sign = HashUtil.jWTHmacSHA256VSign(header, payload, signKey);
			String realtoken = HashUtil.encodeBase64(header) + "." + HashUtil.encodeBase64(payload) + "." + sign;
		
			AccessToken newToken = new AccessToken();
			newToken.setClientInfo(clientInfo);
			newToken.setAccessToken(realtoken);
			newToken.setDateCreated(new Date());
			newToken.setExpireDate(expireDate);
			newToken.setRefreshToken(UUID.randomUUID().toString());// generate refresh code
			newToken.setScope(clientInfo.getScope());
			newToken.setUsername(token.getUsername());
			newToken.setTokenState(0);
			this.accessTokenDAO.save(newToken);
			String tokenJsonTemplate = "{\"access_token\":\"%s\",\"token_type\":\"JWT\",\"expires_in\":%s}";
			String result = String.format(tokenJsonTemplate, newToken.getAccessToken(), ONE_MINUTE_IN_MILLIS,
					newToken.getRefreshToken());
			// ??????result JSON
			return result;
		}
		return null;
	}
	/**
	 * ??????????????? ?????? client id ?????? access_token
	 * 
	 * @param clientId
	 * @param username
	 * @param password
	 * @return
	 */
	public String useUsernamePasswordToChangeAccessToken(String clientId, String username, String password) {
		Optional<ClientInfo> optClientInfo = this.clientInfoDAO.findClientInfoByClientId(clientId, "publish");
		if (!optClientInfo.isPresent()) {
			return null;// error
		}

		ClientInfo clientInfo = optClientInfo.get();
		Optional<ClientInfoDetail> optClientInfoDetail = clientInfo.getClientInfoDetails().stream().findFirst();
		ClientInfoDetail clientInfoDetail = optClientInfoDetail.get();

		String signKey = clientInfoDetail.getSignKey();
		String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

		final long ONE_MINUTE_IN_MILLIS = 600000;// millisecs 60??????
		Calendar date = Calendar.getInstance();
		long t = date.getTimeInMillis();
		Date expireDate = new Date(t + (10 * ONE_MINUTE_IN_MILLIS));

		// client_id , developer_username , username , date_created , expire_date ,
		// scope
		String payloadTemplate = "{\"client_id\":\"%s\",\"developer_username\":\"%s\",\"username\":\"%s\",\"date_created\":\"%s\",\"expire_date\":\"%s\",\"scope\":\"%s\"}";
		String payload = String.format(payloadTemplate, clientId, clientInfo.getDeveloper().getUsername(), username,
				clientInfo.getDateCreated(), expireDate, clientInfo.getScope());
		// TODO use grant code to change access token
		// sign=HMACSHA256(base64(header)+'.'+base64(payload)+'.'+sign_key)
		// Access_token=base64(header)+'.'+base64(payload)+'.'+sign
		String sign = HashUtil.jWTHmacSHA256VSign(header, payload, signKey);
		String realtoken = HashUtil.encodeBase64(header) + "." + HashUtil.encodeBase64(payload) + "." + sign;

		AccessToken token = new AccessToken();
		token.setClientInfo(clientInfo);
		token.setAccessToken(realtoken);
		token.setDateCreated(new Date());
		token.setExpireDate(expireDate);
//		token.setRefreshToken(UUID.randomUUID().toString());// generate refresh code
		token.setScope(clientInfo.getScope());
		token.setUsername(username);
		token.setTokenState(0);
		this.accessTokenDAO.save(token);
		String tokenJsonTemplate = "{\"access_token\":\"%s\",\"token_type\":\"JWT\",\"expires_in\":%s}";
		String result = String.format(tokenJsonTemplate, token.getAccessToken(), ONE_MINUTE_IN_MILLIS,
				token.getRefreshToken());
		// ??????result JSON
		return result;
	}

	/**
	 * ??? grant_code ??? token
	 * 
	 * @param client_id
	 * @return
	 */
	public String useGrantCodeToChangeAccessToken(String clientId, String username, String grant_Code) {
		try {
			Optional<GrantCode> grantCode = this.grantCodeDAO.findGrantCodeByGrantCode(grant_Code);
			GrantCode _grantCode = grantCode.get();
			_grantCode.setCodeState(1);// ?????????
			this.grantCodeDAO.save(_grantCode);

			Optional<ClientInfo> optClientInfo = this.clientInfoDAO.findClientInfoByClientId(clientId, "publish");
			if (!optClientInfo.isPresent()) {
				return null;// error
			}

			ClientInfo clientInfo = optClientInfo.get();
			Optional<ClientInfoDetail> optClientInfoDetail = clientInfo.getClientInfoDetails().stream().findFirst();
			ClientInfoDetail clientInfoDetail = optClientInfoDetail.get();

			String signKey = clientInfoDetail.getSignKey();
			String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

			final long ONE_MINUTE_IN_MILLIS = 600000;// millisecs 60??????
			Calendar date = Calendar.getInstance();
			long t = date.getTimeInMillis();
			Date expireDate = new Date(t + (10 * ONE_MINUTE_IN_MILLIS));

			// client_id , developer_username , username , date_created , expire_date ,
			// scope
			String payloadTemplate = "{\"client_id\":\"%s\",\"developer_username\":\"%s\",\"username\":\"%s\",\"date_created\":\"%s\",\"expire_date\":\"%s\",\"scope\":\"%s\"}";
			String payload = String.format(payloadTemplate, clientId, clientInfo.getDeveloper().getUsername(), username,
					clientInfo.getDateCreated(), expireDate, clientInfo.getScope());
			// TODO use grant code to change access token
			// sign=HMACSHA256(base64(header)+'.'+base64(payload)+'.'+sign_key)
			// Access_token=base64(header)+'.'+base64(payload)+'.'+sign
			String sign = HashUtil.jWTHmacSHA256VSign(header, payload, signKey);
			String realtoken = HashUtil.encodeBase64(header) + "." + HashUtil.encodeBase64(payload) + "." + sign;
			AccessToken token = new AccessToken();
			token.setClientInfo(clientInfo);
			token.setAccessToken(realtoken);
			token.setDateCreated(new Date());
			token.setExpireDate(expireDate);
			token.setRefreshToken(UUID.randomUUID().toString());// generate refresh code
			token.setScope(clientInfo.getScope());
			token.setUsername(username);
			token.setTokenState(0);

			this.accessTokenDAO.save(token);

			String tokenJsonTemplate = "{\"access_token\":\"%s\",\"token_type\":\"JWT\",\"expires_in\":%s,\"refresh_token\":\"%s\"}";
			String result = String.format(tokenJsonTemplate, token.getAccessToken(), ONE_MINUTE_IN_MILLIS,
					token.getRefreshToken());
			// ?????? json token
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param clientId
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkValidPasswordMode(String clientId, String username, String password) {
		// ??????client ??????
		Optional<ClientInfo> optClientInfo = this.clientInfoDAO.findClientInfoByClientId(clientId, "publish");
		if (!optClientInfo.isPresent()) {// ??????client????????????
			return false;
		}
		// ????????????
		UserInfoVO userInfo = new UserInfoVO();
		userInfo.setUsername(username);
		userInfo.setPassword(password);
		if (!oAuthLoginService.validate(userInfo)) {// ??????????????????
			return false;
		}

		return true;
	}

	/**
	 * ?????? grant code ??????
	 * 
	 * @param client_id
	 * @param username
	 * @param grantCode
	 * @return
	 */
	public boolean checkValidGrantMode(String client_id, String grantCode) {
		Optional<GrantCode> optGrantCode = grantCodeDAO.findGrantCodeByGrantCode(grantCode);
		if (!optGrantCode.isPresent()) {// ????????????
			System.out.println("???????????????");
			return false;
		}
		GrantCode grantCodeObj = optGrantCode.get();
		if (!grantCodeObj.isUseable()) {// ????????????
			System.out.println("???????????????");
			return false;
		}
		if (!grantCodeObj.getClientInfo().getClientId().equals(client_id)) {// ?????????????????????client_id
			System.out.println("?????????????????????client_id");
			return false;
		}
		if (!grantCodeObj.getClientInfo().getClientState().equals("publish")) {// ??????????????? ????????????
			System.out.println("???????????????");
			return false;
		}
		if (grantCodeObj.getClientInfo()
				.getScope() != (grantCodeObj.getScope() & grantCodeObj.getClientInfo().getScope())) {// scope ????????????
			System.out.println("scope ?????????");
			return false;
		}
		// ????????????
		return true;
	}

	/**
	 * 
	 * @param clientId
	 * @param username
	 * @return
	 */
	public String generateGrantCodeByClientId(String clientId, String username) {
		String strGrantCode = "";
		Optional<ClientInfo> optClientInfo = this.clientInfoDAO.findClientInfoByClientId(clientId, "publish");
		try {
			if (optClientInfo.isPresent()) {
				ClientInfo clientInfo = optClientInfo.get();
				GrantCode grantCode = new GrantCode();
				grantCode.setClientInfo(clientInfo);
				grantCode.setCodeState(0);// not use
				grantCode.setDateCreated(new Date());

				final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs 10??????
				Calendar date = Calendar.getInstance();
				long t = date.getTimeInMillis();
				Date expireDate = new Date(t + (10 * ONE_MINUTE_IN_MILLIS));
				grantCode.setExpireDate(expireDate);

				grantCode.setGrantCode(UUID.randomUUID().toString());
				grantCode.setScope(clientInfo.getScope());
				grantCode.setUsername(username);

				this.grantCodeDAO.save(grantCode);
				strGrantCode = grantCode.getGrantCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strGrantCode;
	}

	/**
	 * hard code
	 * 
	 * @return
	 */
	public Map<String, String> getClientStateMap() {
		Map<String, String> clientStateList = new LinkedHashMap<String, String>();
		clientStateList.put("init", "?????????");
		clientStateList.put("reviewing", "?????????");
		clientStateList.put("deny", "????????????");
		clientStateList.put("ready", "????????????");
		clientStateList.put("publish", "??????");
		clientStateList.put("stop", "??????");
		return clientStateList;
	}

	public List<Scope> findAllScope() {
		List<Scope> scopes = scopeDAO.listAll();
		return scopes;
	}

	public Map<String, String> findAllClientType() {
		List<ClientType> clientTypes = this.clientTypeDAO.listAll();
		Map<String, String> clientTypeList = new LinkedHashMap<String, String>();
		clientTypes.forEach(obj -> {
			clientTypeList.put(obj.getClientType(), obj.getClientName());
		});
		return clientTypeList;
	}

	public Page<ClientInfo> findPageByClientStatus(String clientState, Integer pageNo, Integer pageSize) {
		try {
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			Criteria criteria = this.clientInfoDAO.createCriteria();
			criteria.add(Restrictions.eq("clientState", clientState));

			Page<ClientInfo> results = this.clientInfoDAO.pageQuery(criteria, pageNo, pageSize);

			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Page<ClientInfo> findPagneByDeveloper(Developer dev, Integer pageNo, Integer pageSize) {
		try {
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			Criteria criteria = this.clientInfoDAO.createCriteria();
			criteria.createAlias("developer", "dev");
			criteria.add(Restrictions.eq("dev.username", dev.getUsername()));

			Page<ClientInfo> results = this.clientInfoDAO.pageQuery(criteria, pageNo, pageSize);

			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ?????????????????????
	 * 
	 * @param obj
	 */
	public void saveOrUpdate(ClientInfo obj) {
		if (obj.isNew()) {
			try {
				this.clientInfoDAO.save(obj);
				ClientInfoDetail detail = new ClientInfoDetail();
				detail.setClientInfo(obj);
				detail.setDateCreated(new Date());
				detail.setLastUpdated(new Date());
				detail.setSignKey(UUID.randomUUID().toString());
				this.clientInfoDetailDAO.save(detail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.clientInfoDAO.update(obj);
		}

	}

	public Optional<ClientInfo> findByClientIdAndClientSecret(String clientId, String clientSecret,
			String clientStatus) {
		return this.clientInfoDAO.findByClientIdAndClientSecret(clientId, clientSecret, "publish");
	}

	/**
	 * ??????id????????????
	 * 
	 * @param id
	 * @return
	 */
	public ClientInfo findById(Integer id) {
		ClientInfo client = this.clientInfoDAO.findById(id);
		client.getDeveloper();
		client.getClientInfoDetails().forEach(it -> {
		});
		return client;
	}

	/**
	 * ??????id????????????
	 * 
	 * @param id
	 */
	public void delete(int id) {
		this.clientInfoDAO.deleteById(id);
	}
}
