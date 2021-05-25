package com.arcare.oauth.po;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the client_info database table.
 * 
 */
@Entity
@Table(name="client_info")
@NamedQuery(name="ClientInfo.findAll", query="SELECT c FROM ClientInfo c")
public class ClientInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="client_id", nullable=false, length=255)
	private String clientId;

	@Column(name="client_secret", nullable=false, length=255)
	private String clientSecret;

	@Column(name="client_state", nullable=false, length=255)
	private String clientState;

	@Column(name="client_type", nullable=false, length=255)
	private String clientType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	private Date dateCreated;

	@Column(name="last_update_user", length=255)
	private String lastUpdateUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_updated")
	private Date lastUpdated;

	@Column(length=255)
	private String note;
	
	@Column(length=255)
	private String reason;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	@Column(name="redirect_uri", nullable=false, length=256)
	private String redirectUri;

	@Column(nullable=false)
	private Integer scope;

	//bi-directional many-to-one association to AccessToken
	@OneToMany(mappedBy="clientInfo")
	private List<AccessToken> accessTokens;

	//bi-directional many-to-one association to Developer
	@ManyToOne
	@JoinColumn(name="developer_id", nullable=false)
	private Developer developer;

	//bi-directional many-to-one association to ClientInfoDetail
	@OneToMany(mappedBy="clientInfo")
	private List<ClientInfoDetail> clientInfoDetails;

	//bi-directional many-to-one association to GrantCode
	@OneToMany(mappedBy="clientInfo")
	private List<GrantCode> grantCodes;

	public ClientInfo() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return this.clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getClientState() {
		return this.clientState;
	}

	public void setClientState(String clientState) {
		this.clientState = clientState;
	}

	public String getClientType() {
		return this.clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getLastUpdateUser() {
		return this.lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRedirectUri() {
		return this.redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public Integer getScope() {
		return this.scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public List<AccessToken> getAccessTokens() {
		return this.accessTokens;
	}

	public void setAccessTokens(List<AccessToken> accessTokens) {
		this.accessTokens = accessTokens;
	}

	public AccessToken addAccessToken(AccessToken accessToken) {
		getAccessTokens().add(accessToken);
		accessToken.setClientInfo(this);

		return accessToken;
	}

	public AccessToken removeAccessToken(AccessToken accessToken) {
		getAccessTokens().remove(accessToken);
		accessToken.setClientInfo(null);

		return accessToken;
	}

	public Developer getDeveloper() {
		return this.developer;
	}

	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}

	public List<ClientInfoDetail> getClientInfoDetails() {
		return this.clientInfoDetails;
	}

	public void setClientInfoDetails(List<ClientInfoDetail> clientInfoDetails) {
		this.clientInfoDetails = clientInfoDetails;
	}

	public ClientInfoDetail addClientInfoDetail(ClientInfoDetail clientInfoDetail) {
		getClientInfoDetails().add(clientInfoDetail);
		clientInfoDetail.setClientInfo(this);

		return clientInfoDetail;
	}

	public ClientInfoDetail removeClientInfoDetail(ClientInfoDetail clientInfoDetail) {
		getClientInfoDetails().remove(clientInfoDetail);
		clientInfoDetail.setClientInfo(null);

		return clientInfoDetail;
	}

	public List<GrantCode> getGrantCodes() {
		return this.grantCodes;
	}

	public void setGrantCodes(List<GrantCode> grantCodes) {
		this.grantCodes = grantCodes;
	}

	public GrantCode addGrantCode(GrantCode grantCode) {
		getGrantCodes().add(grantCode);
		grantCode.setClientInfo(this);

		return grantCode;
	}

	public GrantCode removeGrantCode(GrantCode grantCode) {
		getGrantCodes().remove(grantCode);
		grantCode.setClientInfo(null);

		return grantCode;
	}
	public boolean isNew() {
		if(this.id == null) {
			return true;
		}
		return false;
	}
}