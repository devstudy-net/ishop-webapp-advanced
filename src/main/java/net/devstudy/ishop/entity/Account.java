package net.devstudy.ishop.entity;

import net.devstudy.framework.annotation.jdbc.Column;
import net.devstudy.framework.annotation.jdbc.Table;
import net.devstudy.ishop.model.CurrentAccount;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Table(name="account", nextIdExpression="nextval('account_seq')")
public class Account extends AbstractEntity<Integer> implements CurrentAccount {
	private static final long serialVersionUID = -3196229925974576545L;
	private String name;
	private String email;
	@Column("avatar_url")
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
	
	@Override
	public String getDescription() {
		return name + "("+email+")";
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	@Override
	public String toString() {
		return String.format("Account [id=%s, name=%s, email=%s]", getId(), name, email);
	}
}
