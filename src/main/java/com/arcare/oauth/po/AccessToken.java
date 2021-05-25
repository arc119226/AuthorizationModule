package com.arcare.oauth.po;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the access_token database table.
 * 
 */
@Entity
@Table(name="access_token")
@NamedQuery(name="AccessToken.findAll", query="SELECT a FROM AccessToken a")
public class AccessToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="access_token", nullable=false, length=255)
	private String accessToken;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="expire_date")
	private Date expireDate;

	@Column(name="refresh_token", length=255)
	private String refreshToken;

	private Integer scope;

	@Column(name="token_state", nullable=false)
	private Integer tokenState;

	@Column(length=255)
	private String username;

	//bi-directional many-to-one association to ClientInfo
	@ManyToOne
	@JoinColumn(name="client_id", nullable=false)
	private ClientInfo clientInfo;

	public AccessToken() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Integer getScope() {
		return this.scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public Integer getTokenState() {
		return this.tokenState;
	}

	public void setTokenState(Integer tokenState) {
		this.tokenState = tokenState;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ClientInfo getClientInfo() {
		return this.clientInfo;
	}

	public void setClientInfo(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}

	/**
	 * 是否失效
	 * @return
	 */
	public boolean isUseable() {
		if(this.getTokenState() !=0) {
			return false;
		}
		if(this.expireDate==null) {
			return false;
		}
		return this.expireDate.getTime() > new Date().getTime();
	}
}