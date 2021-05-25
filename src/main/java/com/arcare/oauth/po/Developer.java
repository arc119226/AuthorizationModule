package com.arcare.oauth.po;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the developer database table.
 * 
 */
@Entity
@Table(name="developer")
@NamedQuery(name="Developer.findAll", query="SELECT d FROM Developer d")
public class Developer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	private Date dateCreated;

	@Column(length=255)
	private String email;

	@Column(name="last_update_user", length=255)
	private String lastUpdateUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_updated")
	private Date lastUpdated;

	@Column(length=255)
	private String password;

	@Column(name="user_type", nullable=false, length=255)
	private String userType;

	@Column(nullable=false, length=255)
	private String username;

	//bi-directional many-to-one association to ClientInfo
	@OneToMany(mappedBy="developer")
	private List<ClientInfo> clientInfos;

	public Developer() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<ClientInfo> getClientInfos() {
		return this.clientInfos;
	}

	public void setClientInfos(List<ClientInfo> clientInfos) {
		this.clientInfos = clientInfos;
	}

	public ClientInfo addClientInfo(ClientInfo clientInfo) {
		getClientInfos().add(clientInfo);
		clientInfo.setDeveloper(this);

		return clientInfo;
	}

	public ClientInfo removeClientInfo(ClientInfo clientInfo) {
		getClientInfos().remove(clientInfo);
		clientInfo.setDeveloper(null);

		return clientInfo;
	}
	public boolean isNew() {
		if(this.id == null) {
			return true;
		}
		return false;
	}
}