package net.devstudy.ishop.repository.builder;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class CountProductsSearchFormSQLBuilder extends AbstractSearchFormSQLBuilder {
	@Override
	protected String getSelectFields() {
		return "count(*)";
	}
}
