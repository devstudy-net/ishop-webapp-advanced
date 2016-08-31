package net.devstudy.ishop.repository;

import java.util.List;

import net.devstudy.ishop.entity.Category;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface CategoryRepository {

	List<Category> listAllCategories();
}
