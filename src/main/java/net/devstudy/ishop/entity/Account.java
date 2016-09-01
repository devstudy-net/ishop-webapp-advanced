package net.devstudy.ishop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.devstudy.ishop.model.CurrentAccount;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Entity
@Table(name = "account")
public class Account extends AbstractEntity<Integer> implements CurrentAccount {
	private static final long serialVersionUID = -3196229925974576545L;

	@Id
	@SequenceGenerator(name = "account_seq_generator", sequenceName = "account_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq_generator")
	private Integer id;
	private String name;
	private String email;
	@Column(name = "avatar_url")
	private String avatarUrl;

	public Account() {
		super();
	}

	public Account(String name, String email, String avatarUrl) {
		super();
		this.name = name;
		this.email = email;
		this.avatarUrl = avatarUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	@Override
	public String getDescription() {
		return name + "(" + email + ")";
	}

	@Override
	public String toString() {
		return String.format("Account [id=%s, name=%s, email=%s]", getId(), name, email);
	}
}
