package net.devstudy.ishop.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import net.devstudy.ishop.entity.Category;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface CategoryRepository extends Repository<Category, Integer> {
	
	List<Category> findAll(Sort sort);
}
