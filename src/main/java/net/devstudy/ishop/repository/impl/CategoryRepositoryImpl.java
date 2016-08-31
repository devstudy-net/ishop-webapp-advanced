package net.devstudy.ishop.repository.impl;

import java.util.List;

import net.devstudy.framework.factory.JDBCConnectionUtils;
import net.devstudy.framework.handler.DefaultListResultSetHandler;
import net.devstudy.framework.handler.ResultSetHandler;
import net.devstudy.ishop.entity.Category;
import net.devstudy.ishop.jdbc.JDBCUtils;
import net.devstudy.ishop.repository.CategoryRepository;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class CategoryRepositoryImpl implements CategoryRepository {
	private final ResultSetHandler<List<Category>> categoryListResultSetHandler = new DefaultListResultSetHandler<>(Category.class);
	@Override
	public List<Category> listAllCategories() {
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), "select * from category order by id", categoryListResultSetHandler);
	}
}
