package net.devstudy.ishop.repository;

import java.util.List;

import net.devstudy.framework.annotation.jdbc.CollectionItem;
import net.devstudy.framework.annotation.jdbc.JDBCRepository;
import net.devstudy.framework.annotation.jdbc.Select;
import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.form.SearchForm;
import net.devstudy.ishop.repository.builder.CountProductsSearchFormSQLBuilder;
import net.devstudy.ishop.repository.builder.ListProductsSearchFormSQLBuilder;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@JDBCRepository
public interface ProductRepository {

	@Select("select p.*, c.name as category, pr.name as producer from product p, producer pr, category c where c.id=p.id_category and pr.id=p.id_producer limit ? offset ?")
	@CollectionItem(Product.class)
	List<Product> listAllProducts(int limit, int offset);

	@Select("select count(*) from product")
	int countAllProducts();

	@Select("select p.*, c.name as category, pr.name as producer from product p, category c, producer pr where c.url=? and pr.id=p.id_producer and c.id=p.id_category order by p.id limit ? offset ?")
	@CollectionItem(Product.class)
	List<Product> listProductsByCategory(String categoryUrl, int limit, int offset);

	@Select("select count(p.*) from product p, category c where c.id=p.id_category and c.url=?")
	int countProductsByCategory(String categoryUrl);

	@Select(value = "", sqlBuilderClass = ListProductsSearchFormSQLBuilder.class)
	@CollectionItem(Product.class)
	List<Product> listProductsBySearchForm(SearchForm searchForm, int limit, int offset);

	@Select(value = "", sqlBuilderClass = CountProductsSearchFormSQLBuilder.class)
	int countProductsBySearchForm(SearchForm searchForm);

	@Select("select p.*, c.name as category, pr.name as producer from product p, producer pr, category c where c.id=p.id_category and pr.id=p.id_producer and p.id=?")
	Product findById(int idProduct);
}
