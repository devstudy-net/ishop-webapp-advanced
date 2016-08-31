package net.devstudy.ishop.repository.impl;

import java.util.List;

import net.devstudy.framework.factory.JDBCConnectionUtils;
import net.devstudy.framework.handler.DefaultListResultSetHandler;
import net.devstudy.framework.handler.DefaultUniqueResultSetHandler;
import net.devstudy.framework.handler.IntResultSetHandler;
import net.devstudy.framework.handler.ResultSetHandler;
import net.devstudy.ishop.entity.Order;
import net.devstudy.ishop.jdbc.JDBCUtils;
import net.devstudy.ishop.repository.OrderRepository;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class OrderRepositoryImpl implements OrderRepository {
	private final ResultSetHandler<Order> orderResultSetHandler = new DefaultUniqueResultSetHandler<>(Order.class);
	private final ResultSetHandler<List<Order>> ordersResultSetHandler = new DefaultListResultSetHandler<>(Order.class);
	private final ResultSetHandler<Integer> countResultSetHandler = new IntResultSetHandler();

	@Override
	public void create(Order order) {
		Order createdOrder = JDBCUtils.insert(JDBCConnectionUtils.getCurrentConnection(), 
				"insert into \"order\" values(nextval('order_seq'),?,?)", orderResultSetHandler, order.getIdAccount(), order.getCreated());
		order.setId(createdOrder.getId());
	}

	@Override
	public Order findById(Long id) {
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), "select * from \"order\" where id=?", orderResultSetHandler, id);
	}

	@Override
	public List<Order> listMyOrders(Integer idAccount, int offset, int limit) {
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), "select * from \"order\" where id_account=? order by id desc limit ? offset ?", 
				ordersResultSetHandler, idAccount, limit, offset);
	}

	@Override
	public int countMyOrders(Integer idAccount) {
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), "select count(*) from \"order\" where id_account=?", 
				countResultSetHandler, idAccount);
	}

}
