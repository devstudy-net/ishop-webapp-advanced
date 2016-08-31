package net.devstudy.framework.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import net.devstudy.framework.FrameworkSystemException;
import net.devstudy.framework.SQLBuilder;
import net.devstudy.framework.SearchQuery;
import net.devstudy.framework.annotation.jdbc.CollectionItem;
import net.devstudy.framework.annotation.jdbc.Select;
import net.devstudy.framework.handler.ResultSetHandler;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
class JDBCSelectHelper extends JDBCAbstractSQLHelper {
	Object select(Select select, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
		Class<?> entityClass = getResultEntityClass(method);
		ResultSetHandler<?> resultSetHandler = createResultSetHandler(select.resultSetHandlerClass(), method, entityClass);
		Class<? extends SQLBuilder> sqlBuilderClass = select.sqlBuilderClass();
		if (sqlBuilderClass == SQLBuilder.class) {
			return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), select.value(), resultSetHandler, args);
		} else {
			return select(sqlBuilderClass, resultSetHandler, args);
		}
	}

	private Object select(Class<? extends SQLBuilder> sqlBuilderClass, ResultSetHandler<?> resultSetHandler, Object[] args) 
				throws IllegalAccessException {
		try {
			SQLBuilder sqlBuilder = sqlBuilderClass.newInstance();
			SearchQuery searchQuery = sqlBuilder.build(args);
			LOGGER.debug("Custom SELECT: {}, {}", searchQuery.getSql(), searchQuery.getParams());
			return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), searchQuery.getSql().toString(), resultSetHandler, searchQuery.getParams().toArray());
		} catch (InstantiationException e) {
			throw new FrameworkSystemException("Can't create instance of SQLBuilder for class: " + sqlBuilderClass, e);
		}
	}

	private Class<?> getResultEntityClass(Method method) {
		CollectionItem collectionItem = method.getAnnotation(CollectionItem.class);
		if (collectionItem != null) {
			return collectionItem.value();
		} else {
			Class<?> returnType = method.getReturnType();
			if (returnType.isArray()) {
				throw new FrameworkSystemException("Use collections instead of array for method: " + method);
			} else if (Collection.class.isAssignableFrom(returnType)) {
				throw new FrameworkSystemException("Use @CollectionItem annotation to specify collection item type for method: " + method);
			} else {
				return returnType;
			}
		}
	}
}
