package net.devstudy.ishop.repository;

import java.util.List;

import net.devstudy.framework.annotation.jdbc.CollectionItem;
import net.devstudy.framework.annotation.jdbc.Insert;
import net.devstudy.framework.annotation.jdbc.JDBCRepository;
import net.devstudy.framework.annotation.jdbc.Select;
import net.devstudy.ishop.entity.Order;
import net.devstudy.ishop.repository.handler.OrderResultSetHandler;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@JDBCRepository
public interface OrderRepository {

	@Insert
	void create(Order order);
	
	@Select(value="select ord.created, ord.id_account, o.id as oid, o.id_order as id_order, o.id_product, o.count, p.*, c.name as "
			+ "category, pr.name as producer from \"order\" ord, order_item o, product p, category c, producer pr where pr.id=p.id_producer "
			+ "and c.id=p.id_category and o.id_product=p.id and o.id_order=? and o.id_order=ord.id",
			resultSetHandlerClass=OrderResultSetHandler.class)
	Order findById(Long id);
	
	@Select("select * from \"order\" where id_account=? order by id desc limit ? offset ?")
	@CollectionItem(Order.class)
	List<Order> listMyOrders(Integer idAccount, int limit, int offset);

	@Select("select count(*) from \"order\" where id_account=?")
	int countMyOrders(Integer idAccount);
}
