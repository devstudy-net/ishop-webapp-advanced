package net.devstudy.framework.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class IntResultSetHandler implements ResultSetHandler<Integer> {
	@Override
	public Integer handle(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return rs.getInt(1);
		} else {
			return 0;
		}
	}
}
