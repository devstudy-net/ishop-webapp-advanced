package net.devstudy.ishop.entity;

import net.devstudy.framework.annotation.jdbc.Column;
import net.devstudy.framework.annotation.jdbc.Table;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Table(name="producer")
public class Producer extends AbstractEntity<Integer> {
	private static final long serialVersionUID = -4967160259057526492L;
	private String name;
	@Column("product_count")
	private Integer productCount;
	
	public Producer() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getProductCount() {
		return productCount;
	}
	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}
}
