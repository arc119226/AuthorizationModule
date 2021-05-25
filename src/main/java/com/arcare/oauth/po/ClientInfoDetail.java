package com.arcare.oauth.po;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the client_info_detail database table.
 * 
 */
@Entity
@Table(name="client_info_detail")
@NamedQuery(name="ClientInfoDetail.findAll", query="SELECT c FROM ClientInfoDetail c")
public class ClientInfoDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_updated")
	private Date lastUpdated;

	@Column(name="sign_key", nullable=false, length=255)
	private String signKey;

	//bi-directional many-to-one association to ClientInfo
	@ManyToOne
	@JoinColumn(name="client_id", nullable=false)
	private ClientInfo clientInfo;

	public ClientInfoDetail() {
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

	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getSignKey() {
		return this.signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public ClientInfo getClientInfo() {
		return this.clientInfo;
	}

	public void setClientInfo(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}

}