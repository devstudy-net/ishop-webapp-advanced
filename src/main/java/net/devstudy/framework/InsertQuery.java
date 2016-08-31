package net.devstudy.framework;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class InsertQuery extends SearchQuery {
	private Field idField;

	public InsertQuery(Field idField, StringBuilder sql, List<Object> params) {
		super(sql, params);
		this.idField = idField;
	}

	public Field getIdField() {
		return idField;
	}
}
