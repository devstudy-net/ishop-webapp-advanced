package net.devstudy.ishop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(readOnly=true)
@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProducerRepository producerRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Product> listAllProducts(int page, int limit) {
		return productRepository.findAll(new PageRequest(page - 1, limit)).getContent();
	}

	@Override
	public List<Product> listProductsByCategory(String categoryUrl, int page, int limit) {
		return productRepository.findByCategoryUrl(categoryUrl, new PageRequest(page - 1, limit));
	}

	@Override
	public List<Category> listAllCategories() {
		return categoryRepository.findAll(new Sort("id"));
	}

	@Override
	public List<Producer> listAllProducers() {
		return producerRepository.findAll(new Sort("name"));
	}
	
	@Override
	public int countAllProducts() {
		return (int)productRepository.count();
	}
	
	@Override
	public int countProductsByCategory(String categoryUrl) {
		return productRepository.countByCategoryUrl(categoryUrl);
	}
	
	@Override
	public List<Product> listProductsBySearchForm(SearchForm searchForm, int page, int limit) {
		return productRepository.listProductsBySearchForm(searchForm, new PageRequest(page - 1, limit));
	}

	@Override
	public int countProductsBySearchForm(SearchForm searchForm) {
		return productRepository.countProductsBySearchForm(searchForm);
	}
}
