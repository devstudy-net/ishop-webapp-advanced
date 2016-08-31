package net.devstudy.ishop.repository.builder;

import java.util.ArrayList;
import java.util.List;

import net.devstudy.framework.SearchQuery;
import net.devstudy.framework.SQLBuilder;
import net.devstudy.ishop.form.SearchForm;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public abstract class AbstractSearchFormSQLBuilder implements SQLBuilder {

	@Override
	public final SearchQuery build(Object... builderParams) {
		SearchForm searchForm = (SearchForm) builderParams[0];
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select ");
		sql.append(getSelectFields()).append(" from product p, category c, producer pr where pr.id=p.id_producer and c.id=p.id_category and (p.name ilike ? or p.description ilike ?)");
		params.add("%" + searchForm.getQuery() + "%");
		params.add("%" + searchForm.getQuery() + "%");
		populateSqlAndParams(sql, params, searchForm.getCategories(), "c.id = ?");
		populateSqlAndParams(sql, params, searchForm.getProducers(), "pr.id = ?");
		sql.append(getLastSqlPart());
		for (int i = 1; i < builderParams.length; i++) {
			params.add(builderParams[i]);
		}
		return new SearchQuery(sql, params);
	}
	
	protected void populateSqlAndParams(StringBuilder sql, List<Object> params, List<Integer> list, String expression) {
		if (list != null && !list.isEmpty()) {
			sql.append(" and (");
			for (int i = 0; i < list.size(); i++) {
				sql.append(expression);
				params.add(list.get(i));
				if (i != list.size() - 1) {
					sql.append(" or ");
				}
			}
			sql.append(")");
		}
	}

	protected abstract String getSelectFields();

	protected String getLastSqlPart() {
		return "";
	}
}
