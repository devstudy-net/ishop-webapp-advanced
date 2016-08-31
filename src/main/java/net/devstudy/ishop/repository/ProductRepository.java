package net.devstudy.ishop.repository;

import java.util.List;

import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.form.SearchForm;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface ProductRepository {

	List<Product> listAllProducts(int offset, int limit);

	int countAllProducts();

	List<Product> listProductsByCategory(String categoryUrl, int offset, int limit);

	int countProductsByCategory(String categoryUrl);

	List<Product> listProductsBySearchForm(SearchForm searchForm, int offset, int limit);

	int countProductsBySearchForm(SearchForm searchForm);
	
	Product findById(int idProduct);
}
