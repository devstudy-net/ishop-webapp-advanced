package net.devstudy.ishop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.form.SearchForm;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@NoRepositoryBean
public interface SearchProductRepository {

	List<Product> listProductsBySearchForm(SearchForm searchForm, Pageable pageable);

	int countProductsBySearchForm(SearchForm searchForm);
}
