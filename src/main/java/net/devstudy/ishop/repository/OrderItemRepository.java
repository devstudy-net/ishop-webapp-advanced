package net.devstudy.ishop.repository;

import java.util.List;

import net.devstudy.ishop.entity.OrderItem;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface OrderItemRepository {

	List<OrderItem> findByIdOrder(Long idOrder);
	
	void create(OrderItem orderItem);
}
