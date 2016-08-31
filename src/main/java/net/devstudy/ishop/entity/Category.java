package net.devstudy.ishop.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import net.devstudy.framework.annotation.jdbc.Column;
import net.devstudy.framework.annotation.jdbc.Table;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@XmlRootElement(name = "category")
@Table(name="category")
public class Category extends AbstractEntity<Integer> {
	private static final long serialVersionUID = 5709707193151272208L;
	private String name;
	private String url;
	@Column("product_count")
	private Integer productCount;

	public Category() {
		super();
	}

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	@XmlAttribute
	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getProductCount() {
		return productCount;
	}

	@XmlAttribute(name="product-count")
	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}
	
	@XmlAttribute
	@Override
	public void setId(Integer id) {
		super.setId(id);
	}

	@Override
	public String toString() {
		return String.format("Category [id=%s, name=%s, url=%s, productCount=%s]", getId(), name, url, productCount);
	}
}
