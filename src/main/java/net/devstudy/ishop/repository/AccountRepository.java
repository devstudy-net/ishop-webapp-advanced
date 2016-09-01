package net.devstudy.ishop.repository;

import org.springframework.data.repository.CrudRepository;

import net.devstudy.ishop.entity.Account;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface AccountRepository extends CrudRepository<Account, Integer> {

	Account findByEmail(String email);
}
