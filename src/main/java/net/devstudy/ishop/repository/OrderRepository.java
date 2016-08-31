package net.devstudy.ishop.repository;

import java.util.List;

import net.devstudy.ishop.entity.Order;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface OrderRepository {

	void create(Order order);
	
	Order findById(Long id);
	
	List<Order> listMyOrders(Integer idAccount, int offset, int limit);

	int countMyOrders(Integer idAccount);
}
