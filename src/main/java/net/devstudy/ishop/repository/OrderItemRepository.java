package net.devstudy.ishop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.devstudy.ishop.entity.OrderItem;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {

	List<OrderItem> findByIdOrder(Long idOrder);
}
