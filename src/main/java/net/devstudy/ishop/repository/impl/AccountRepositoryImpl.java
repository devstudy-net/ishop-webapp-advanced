package net.devstudy.ishop.repository.impl;

import net.devstudy.framework.factory.JDBCConnectionUtils;
import net.devstudy.framework.handler.DefaultUniqueResultSetHandler;
import net.devstudy.framework.handler.ResultSetHandler;
import net.devstudy.ishop.entity.Account;
import net.devstudy.ishop.jdbc.JDBCUtils;
import net.devstudy.ishop.repository.AccountRepository;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class AccountRepositoryImpl implements AccountRepository {
	private final ResultSetHandler<Account> accountResultSetHandler = new DefaultUniqueResultSetHandler<>(Account.class);
	
	@Override
	public Account findByEmail(String email) {
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), "select * from account where email=?", accountResultSetHandler, email);
	}

	@Override
	public void create(Account account) {
		Account createdAccount = JDBCUtils.insert(JDBCConnectionUtils.getCurrentConnection(), 
				"insert into account values (nextval('account_seq'),?,?,?)", accountResultSetHandler, 
				account.getName(), account.getEmail(), account.getAvatarUrl());
		account.setId(createdAccount.getId());
	}
}
