package net.devstudy.ishop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devstudy.framework.annotation.jdbc.Transactional;
import net.devstudy.framework.factory.JDBCConnectionUtils;
import net.devstudy.framework.handler.DefaultListResultSetHandler;
import net.devstudy.framework.handler.IntResultSetHandler;
import net.devstudy.framework.handler.ResultSetHandler;
import net.devstudy.ishop.entity.Category;
import net.devstudy.ishop.entity.Producer;
import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.form.SearchForm;
import net.devstudy.ishop.jdbc.JDBCUtils;
import net.devstudy.ishop.jdbc.SearchQuery;
import net.devstudy.ishop.service.ProductService;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Transactional
public class ProductServiceImpl implements ProductService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	private final ResultSetHandler<List<Product>> productsResultSetHandler = new DefaultListResultSetHandler<>(Product.class);
	private final ResultSetHandler<List<Category>> categoryListResultSetHandler = new DefaultListResultSetHandler<>(Category.class);
	private final ResultSetHandler<List<Producer>> producerListResultSetHandler = new DefaultListResultSetHandler<>(Producer.class);
	private final ResultSetHandler<Integer> countResultSetHandler = new IntResultSetHandler();

	public ProductServiceImpl() {
		super();
	}

	@Override
	public List<Product> listAllProducts(int page, int limit) {
		int offset = (page - 1) * limit;
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(),
				"select p.*, c.name as category, pr.name as producer from product p, producer pr, category c "
						+ "where c.id=p.id_category and pr.id=p.id_producer limit ? offset ?",
				productsResultSetHandler, limit, offset);
	}

	@Override
	public List<Product> listProductsByCategory(String categoryUrl, int page, int limit) {
		int offset = (page - 1) * limit;
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(),
				"select p.*, c.name as category, pr.name as producer from product p, category c, producer pr where c.url=? and pr.id=p.id_producer and c.id=p.id_category order by p.id limit ? offset ?",
				productsResultSetHandler, categoryUrl, limit, offset);
	}

	@Override
	public List<Category> listAllCategories() {
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), "select * from category order by id", categoryListResultSetHandler);
	}

	@Override
	public List<Producer> listAllProducers() {
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), "select * from producer order by name", producerListResultSetHandler);
	}

	@Override
	public int countAllProducts() {
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), "select count(*) from product", countResultSetHandler);
	}

	@Override
	public int countProductsByCategory(String categoryUrl) {
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(),
				"select count(p.*) from product p, category c where c.id=p.id_category and c.url=?",
				countResultSetHandler, categoryUrl);
	}

	@Override
	public List<Product> listProductsBySearchForm(SearchForm form, int page, int limit) {
		int offset = (page - 1) * limit;
		SearchQuery sq = buildSearchQuery("p.*, c.name as category, pr.name as producer", form);
		sq.getSql().append(" order by p.id limit ? offset ?");
		sq.getParams().add(limit);
		sq.getParams().add(offset);
		LOGGER.debug("search query={} with params={}", sq.getSql(), sq.getParams());
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), sq.getSql().toString(), productsResultSetHandler, sq.getParams().toArray());
	}

	protected SearchQuery buildSearchQuery(String selectFields, SearchForm form) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select ");
		sql.append(selectFields).append(
				" from product p, category c, producer pr where pr.id=p.id_producer and c.id=p.id_category and (p.name ilike ? or p.description ilike ?)");
		params.add("%" + form.getQuery() + "%");
		params.add("%" + form.getQuery() + "%");
		JDBCUtils.populateSqlAndParams(sql, params, form.getCategories(), "c.id = ?");
		JDBCUtils.populateSqlAndParams(sql, params, form.getProducers(), "pr.id = ?");
		return new SearchQuery(sql, params);
	}

	@Override
	public int countProductsBySearchForm(SearchForm form) {
		SearchQuery sq = buildSearchQuery("count(*)", form);
		LOGGER.debug("search query={} with params={}", sq.getSql(), sq.getParams());
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), sq.getSql().toString(), countResultSetHandler, sq.getParams().toArray());
	}
}
