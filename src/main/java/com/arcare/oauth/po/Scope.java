package com.arcare.oauth.po;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the scope database table.
 * 
 */
@Entity
@Table(name="scope")
@NamedQuery(name="Scope.findAll", query="SELECT s FROM Scope s")
public class Scope implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Lob
	private String note;

	@Column(name="scope_name", length=50)
	private String scopeName;

	@Column(name="scope_value")
	private Integer scopeValue;

	public Scope() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getScopeName() {
		return this.scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public Integer getScopeValue() {
		return this.scopeValue;
	}

	public void setScopeValue(Integer scopeValue) {
		this.scopeValue = scopeValue;
	}

}