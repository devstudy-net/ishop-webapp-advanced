package net.devstudy.ishop.service.impl;

import java.util.List;

import net.devstudy.framework.annotation.jdbc.Transactional;
import net.devstudy.ishop.entity.Category;
import net.devstudy.ishop.entity.Producer;
import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.form.SearchForm;
import net.devstudy.ishop.repository.CategoryRepository;
import net.devstudy.ishop.repository.ProducerRepository;
import net.devstudy.ishop.repository.ProductRepository;
import net.devstudy.ishop.service.ProductService;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Transactional
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final ProducerRepository producerRepository;
	private final CategoryRepository categoryRepository;
	
	public ProductServiceImpl(ServiceManager serviceManager) {
		super();
		this.productRepository = serviceManager.productRepository;
		this.producerRepository = serviceManager.producerRepository;
		this.categoryRepository = serviceManager.categoryRepository;
	}

	@Override
	public List<Product> listAllProducts(int page, int limit) {
		int offset = (page - 1) * limit;
		return productRepository.listAllProducts(offset, limit);
	}

	@Override
	public List<Product> listProductsByCategory(String categoryUrl, int page, int limit) {
		int offset = (page - 1) * limit;
		return productRepository.listProductsByCategory(categoryUrl, offset, limit);
	}

	@Override
	public List<Category> listAllCategories() {
		return categoryRepository.listAllCategories();
	}

	@Override
	public List<Producer> listAllProducers() {
		return producerRepository.listAllProducers();
	}
	
	@Override
	public int countAllProducts() {
		return productRepository.countAllProducts();
	}
	
	@Override
	public int countProductsByCategory(String categoryUrl) {
		return productRepository.countProductsByCategory(categoryUrl);
	}
	
	@Override
	public List<Product> listProductsBySearchForm(SearchForm searchForm, int page, int limit) {
		int offset = (page - 1) * limit;
		return productRepository.listProductsBySearchForm(searchForm, offset, limit);
	}

	@Override
	public int countProductsBySearchForm(SearchForm searchForm) {
		return productRepository.countProductsBySearchForm(searchForm);
	}
}
