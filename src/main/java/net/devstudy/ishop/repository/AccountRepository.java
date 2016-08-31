package net.devstudy.ishop.repository;

import net.devstudy.framework.annotation.jdbc.Insert;
import net.devstudy.framework.annotation.jdbc.Select;
import net.devstudy.ishop.entity.Account;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface AccountRepository {

	@Select("select * from account where email=?")
	Account findByEmail(String email);
	
	@Insert
	void create(Account account);
}
