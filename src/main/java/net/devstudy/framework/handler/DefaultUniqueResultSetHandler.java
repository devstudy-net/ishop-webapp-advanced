package net.devstudy.framework.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class DefaultUniqueResultSetHandler<T> implements ResultSetHandler<T> {
	private ResultSetHandler<T> oneRowResultSetHandler;

	public DefaultUniqueResultSetHandler(ResultSetHandler<T> oneRowResultSetHandler) {
		super();
		this.oneRowResultSetHandler = oneRowResultSetHandler;
	}

	public DefaultUniqueResultSetHandler(Class<T> entityClass) {
		this(new DefaultResultSetHandler<>(entityClass));
	}

	@Override
	public T handle(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return oneRowResultSetHandler.handle(rs);
		} else {
			return null;
		}
	}
}
