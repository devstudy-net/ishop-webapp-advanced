package net.devstudy.ishop.repository;

import java.util.List;

import net.devstudy.framework.annotation.jdbc.CollectionItem;
import net.devstudy.framework.annotation.jdbc.Insert;
import net.devstudy.framework.annotation.jdbc.JDBCRepository;
import net.devstudy.framework.annotation.jdbc.Select;
import net.devstudy.ishop.entity.OrderItem;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@JDBCRepository
public interface OrderItemRepository {

	@Select("select o.id, o.id_order as id_order, o.id_product, o.count, p.id as id_product, p.name, p.description, p.price, p.image_link, "
				+ "c.name as category, pr.name as producer from order_item o, product p, category c, producer pr "
				+ "where pr.id=p.id_producer and c.id=p.id_category and o.id_product=p.id and o.id_order=?")
	@CollectionItem(OrderItem.class)
	List<OrderItem> findByIdOrder(Long idOrder);
	
	@Insert
	void create(OrderItem orderItem);
}
