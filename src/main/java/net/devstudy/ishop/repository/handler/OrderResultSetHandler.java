package net.devstudy.ishop.repository.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.devstudy.framework.handler.ResultSetHandler;
import net.devstudy.ishop.entity.Order;
import net.devstudy.ishop.entity.OrderItem;
import net.devstudy.ishop.entity.Product;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class OrderResultSetHandler implements ResultSetHandler<Order> {
	@Override
	public Order handle(ResultSet rs) throws SQLException {
		Order o = new Order();
		o.setItems(new ArrayList<>());
		do {
			o.setId(rs.getLong("id_order"));
			o.setCreated(rs.getTimestamp("created"));
			o.setIdAccount(rs.getInt("id_account"));
			OrderItem orderItem = new OrderItem();
			orderItem.setId(rs.getLong("oid"));
			orderItem.setCount(rs.getInt("count"));
			orderItem.setIdOrder(rs.getLong("id_order"));
			Product p = new Product();
			p.setCategory(rs.getString("category"));
			p.setDescription(rs.getString("description"));
			p.setId(rs.getInt("id_product"));
			p.setImageLink(rs.getString("image_link"));
			p.setName(rs.getString("name"));
			p.setPrice(rs.getBigDecimal("price"));
			p.setProducer(rs.getString("producer"));
			orderItem.setProduct(p);
			o.getItems().add(orderItem);
		} while (rs.next());
		return o;
	}
}
