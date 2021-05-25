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

			final long ONE_MINUTE_IN_MILLIS = 600000;// millisecs 60分鐘
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
			// 回傳result JSON
			return result;
		}
		return null;
	}
	/**
	 * 用帳號密碼 以及 client id 去換 access_token
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

		final long ONE_MINUTE_IN_MILLIS = 600000;// millisecs 60分鐘
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
		// 回傳result JSON
		return result;
	}

	/**
	 * 用 grant_code 換 token
	 * 
	 * @param client_id
	 * @return
	 */
	public String useGrantCodeToChangeAccessToken(String clientId, String username, String grant_Code) {
		try {
			Optional<GrantCode> grantCode = this.grantCodeDAO.findGrantCodeByGrantCode(grant_Code);
			GrantCode _grantCode = grantCode.get();
			_grantCode.setCodeState(1);// 使用過
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

			final long ONE_MINUTE_IN_MILLIS = 600000;// millisecs 60分鐘
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
			// 回傳 json token
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
		// 驗證client 狀態
		Optional<ClientInfo> optClientInfo = this.clientInfoDAO.findClientInfoByClientId(clientId, "publish");
		if (!optClientInfo.isPresent()) {// 驗證client是否發佈
			return false;
		}
		// 驗證帳密
		UserInfoVO userInfo = new UserInfoVO();
		userInfo.setUsername(username);
		userInfo.setPassword(password);
		if (!oAuthLoginService.validate(userInfo)) {// 不是合法會員
			return false;
		}

		return true;
	}

	/**
	 * 驗證 grant code 合法
	 * 
	 * @param client_id
	 * @param username
	 * @param grantCode
	 * @return
	 */
	public boolean checkValidGrantMode(String client_id, String grantCode) {
		Optional<GrantCode> optGrantCode = grantCodeDAO.findGrantCodeByGrantCode(grantCode);
		if (!optGrantCode.isPresent()) {// 物件存在
			System.out.println("物件不存在");
			return false;
		}
		GrantCode grantCodeObj = optGrantCode.get();
		if (!grantCodeObj.isUseable()) {// 是否可用
			System.out.println("物件不可用");
			return false;
		}
		if (!grantCodeObj.getClientInfo().getClientId().equals(client_id)) {// 是否對應相同的client_id
			System.out.println("沒有對應相同的client_id");
			return false;
		}
		if (!grantCodeObj.getClientInfo().getClientState().equals("publish")) {// 是否為發布 可用狀態
			System.out.println("不可用狀態");
			return false;
		}
		if (grantCodeObj.getClientInfo()
				.getScope() != (grantCodeObj.getScope() & grantCodeObj.getClientInfo().getScope())) {// scope 是否包含
			System.out.println("scope 不符合");
			return false;
		}
		// 通過檢查
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

				final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs 10分鐘
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
		clientStateList.put("init", "編輯中");
		clientStateList.put("reviewing", "審核中");
		clientStateList.put("deny", "審核失敗");
		clientStateList.put("ready", "審核通過");
		clientStateList.put("publish", "發佈");
		clientStateList.put("stop", "停用");
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
	 * 存檔或新增一筆
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
	 * 依據id取得物件
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
	 * 依據id進行刪除
	 * 
	 * @param id
	 */
	public void delete(int id) {
		this.clientInfoDAO.deleteById(id);
	}
}
