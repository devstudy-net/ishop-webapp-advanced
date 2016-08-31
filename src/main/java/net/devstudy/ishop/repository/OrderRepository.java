package net.devstudy.ishop.repository;

import java.util.List;

import net.devstudy.framework.annotation.jdbc.CollectionItem;
import net.devstudy.framework.annotation.jdbc.Insert;
import net.devstudy.framework.annotation.jdbc.JDBCRepository;
import net.devstudy.framework.annotation.jdbc.Select;
import net.devstudy.ishop.entity.Order;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@JDBCRepository
public interface OrderRepository {

	@Insert
	void create(Order order);
	
	@Select("select * from \"order\" where id=?")
	Order findById(Long id);
	
	@Select("select * from \"order\" where id_account=? order by id desc limit ? offset ?")
	@CollectionItem(Order.class)
	List<Order> listMyOrders(Integer idAccount, int limit, int offset);

	@Select("select count(*) from \"order\" where id_account=?")
	int countMyOrders(Integer idAccount);
}
