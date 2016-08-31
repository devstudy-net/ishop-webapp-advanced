package net.devstudy.ishop.repository.impl;

import java.util.List;

import net.devstudy.framework.factory.JDBCConnectionUtils;
import net.devstudy.framework.handler.DefaultListResultSetHandler;
import net.devstudy.framework.handler.DefaultUniqueResultSetHandler;
import net.devstudy.framework.handler.ResultSetHandler;
import net.devstudy.ishop.entity.OrderItem;
import net.devstudy.ishop.jdbc.JDBCUtils;
import net.devstudy.ishop.repository.OrderItemRepository;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class OrderItemRepositoryImpl implements OrderItemRepository {
	private final ResultSetHandler<List<OrderItem>> orderItemListResultSetHandler = new DefaultListResultSetHandler<>(OrderItem.class);
	private final ResultSetHandler<OrderItem> orderItemResultSetHandler = new DefaultUniqueResultSetHandler<>(OrderItem.class);
	
	@Override
	public List<OrderItem> findByIdOrder(Long idOrder) {
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(),
				"select o.id, o.id_order as id_order, o.id_product, o.count, p.id as pid, p.name, p.description, p.price, p.image_link, "
				+ "c.name as category, pr.name as producer from order_item o, product p, category c, producer pr "
				+ "where pr.id=p.id_producer and c.id=p.id_category and o.id_product=p.id and o.id_order=?",
				orderItemListResultSetHandler, idOrder);
	}

	@Override
	public void create(OrderItem orderItem) {
		OrderItem createdOrderItem = JDBCUtils.insert(JDBCConnectionUtils.getCurrentConnection(), 
				"insert into order_item values(nextval('order_item_seq'),?,?,?)", orderItemResultSetHandler, 
				orderItem.getId(), orderItem.getProduct().getId(), orderItem.getCount());
		orderItem.setId(createdOrderItem.getId());
	}
}
