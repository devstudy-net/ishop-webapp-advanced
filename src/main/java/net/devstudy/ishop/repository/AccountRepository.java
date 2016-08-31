package net.devstudy.ishop.repository;

import net.devstudy.ishop.entity.Account;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface AccountRepository {

	Account findByEmail(String email);
	
	void create(Account account);
}
