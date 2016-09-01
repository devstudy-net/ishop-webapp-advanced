package net.devstudy.ishop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import net.devstudy.ishop.entity.Order;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface OrderRepository extends CrudRepository<Order, Long> {

	Order findById(Long id);
	
	List<Order> findByIdAccount(Integer idAccount, Pageable pageble);

	int countByIdAccount(Integer idAccount);
}
