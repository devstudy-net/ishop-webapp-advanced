package net.devstudy.ishop.entity;

import net.devstudy.framework.annotation.jdbc.Child;
import net.devstudy.framework.annotation.jdbc.Column;
import net.devstudy.framework.annotation.jdbc.Table;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Table(name="order_item", nextIdExpression="nextval('order_item_seq')")
public class OrderItem extends AbstractEntity<Long>{
	private static final long serialVersionUID = -365373848626193474L;
	@Column("id_order")
	private Long idOrder;
	@Child(columnName="id_product")
	private Product product;
	private Integer count;

	public OrderItem(Long idOrder, Product product, Integer count) {
		super();
		this.idOrder = idOrder;
		this.product = product;
		this.count = count;
	}

	public OrderItem() {
		super();
	}

	public Long getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(Long idOrder) {
		this.idOrder = idOrder;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return String.format("OrderItem [id=%s, idOrder=%s, product=%s, count=%s]", getId(), idOrder, product, count);
	}
}
