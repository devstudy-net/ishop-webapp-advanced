package net.devstudy.ishop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import net.devstudy.ishop.entity.Product;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>, SearchProductRepository {
	
	List<Product> findByCategoryUrl(String url, Pageable pageable);

	int countByCategoryUrl(String url);
	
	Product findById(int idProduct);
}
