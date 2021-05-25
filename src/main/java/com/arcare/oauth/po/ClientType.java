package com.arcare.oauth.po;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the client_type database table.
 * 
 */
@Entity
@Table(name="client_type")
@NamedQuery(name="ClientType.findAll", query="SELECT c FROM ClientType c")
public class ClientType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="client_name", length=50)
	private String clientName;

	@Column(name="client_type", length=50)
	private String clientType;

	public ClientType() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientType() {
		return this.clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

}