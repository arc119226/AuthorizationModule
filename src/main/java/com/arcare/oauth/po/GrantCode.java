package com.arcare.oauth.po;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the grant_code database table.
 * 
 */
@Entity
@Table(name="grant_code")
@NamedQuery(name="GrantCode.findAll", query="SELECT g FROM GrantCode g")
public class GrantCode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="code_state")
	private Integer codeState;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="expire_date")
	private Date expireDate;

	@Column(name="grant_code", nullable=false, length=255)
	private String grantCode;

	@Column(nullable=false)
	private Integer scope;

	@Column(nullable=false, length=255)
	private String username;

	//bi-directional many-to-one association to ClientInfo
	@ManyToOne
	@JoinColumn(name="client_id", nullable=false)
	private ClientInfo clientInfo;

	public GrantCode() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodeState() {
		return this.codeState;
	}

	public void setCodeState(Integer codeState) {
		this.codeState = codeState;
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

	public String getGrantCode() {
		return this.grantCode;
	}

	public void setGrantCode(String grantCode) {
		this.grantCode = grantCode;
	}

	public int getScope() {
		return this.scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
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
		if(this.getCodeState() !=0) {
			return false;
		}
		if(this.expireDate==null) {
			return false;
		}
		return this.expireDate.getTime() > new Date().getTime();
	}
}