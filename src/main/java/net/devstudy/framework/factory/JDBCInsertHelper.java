package net.devstudy.framework.factory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.devstudy.framework.FrameworkSystemException;
import net.devstudy.framework.InsertQuery;
import net.devstudy.framework.annotation.jdbc.Child;
import net.devstudy.framework.annotation.jdbc.Insert;
import net.devstudy.framework.annotation.jdbc.Table;
import net.devstudy.framework.handler.ResultSetHandler;
import net.devstudy.framework.util.ReflectionUtils;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@SuppressWarnings("rawtypes")
class JDBCInsertHelper extends JDBCAbstractSQLHelper {
	Object insert(Insert insert, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
		validateMethodArgs(method, args);
		Object entity = args[0];
		Class<?> entityClass = entity.getClass();
		Table table = entityClass.getAnnotation(Table.class);
		validateTableAnnotation(table, entityClass);
		insertEntity(entity, table, method, insert.resultSetHandlerClass());
		return null;
	}

	private void validateMethodArgs(Method method, Object[] args) {
		if (args.length != 1) {
			throw new FrameworkSystemException("Method with @Insert annotation: " + method + " should have one argument only!");
		}
	}

	private void validateTableAnnotation(Table table, Class<?> entityClass) {
		if (table == null) {
			throw new FrameworkSystemException("Entity class " + entityClass + " does not have @Table annotation!");
		}
		if (table.nextIdExpression().isEmpty()) {
			throw new FrameworkSystemException("@Table annotation for entity class " + entityClass + " should contain nextIdExpression parameter!");
		}
	}

	private void insertEntity(Object entity, Table table, Method method, Class<? extends ResultSetHandler> resultSetHandlerClass)
			throws IllegalAccessException, InvocationTargetException {
		List<Field> fields = ReflectionUtils.getAccessibleEntityFields(entity.getClass());
		InsertQuery sql = build(entity, table, fields);
		LOGGER.debug("INSERT: {}, {}", sql.getSql(), sql.getParams());
		ResultSetHandler<?> resultSetHandler = createResultSetHandler(resultSetHandlerClass, method, entity.getClass());
		Object insertedEntity = JDBCUtils.insert(JDBCConnectionUtils.getCurrentConnection(), sql.getSql().toString(), 
				resultSetHandler, sql.getParams().toArray());
		updateIdField(entity, insertedEntity, sql.getIdField(), table.id());
	}

	private InsertQuery build(Object entity, Table table, List<Field> fields) throws IllegalAccessException {
		StringBuilder sql = new StringBuilder("insert into ").append(table.name()).append("(");
		StringBuilder values = new StringBuilder(" values (");
		Field idField = null;
		List<Object> params = new ArrayList<>();
		for (Field field : fields) {
			if (field.getName().equals(table.id())) {
				idField = field;
				values.append(table.nextIdExpression()).append(',');
			} else {
				values.append("?,");
				params.add(getFieldValue(field, entity));
			}
			sql.append(getColumnName(field)).append(',');
		}
		sql.deleteCharAt(sql.length() - 1);
		values.deleteCharAt(values.length() - 1);
		sql.append(')').append(values).append(')');
		return new InsertQuery(idField, sql, params);
	}

	private String getColumnName(Field field) {
		Child child = field.getAnnotation(Child.class);
		if (child != null) {
			return child.columnName();
		} else {
			return ReflectionUtils.getColumnNameForField(field);
		}
	}

	private Object getFieldValue(Field field, Object entity) throws IllegalAccessException {
		Child child = field.getAnnotation(Child.class);
		Object fieldValue = field.get(entity);
		if (child != null) {
			List<Field> fields = ReflectionUtils.getAccessibleEntityFields(fieldValue.getClass());
			Field idField = ReflectionUtils.findField(fieldValue.getClass(), fields, child.idFieldName());
			return idField.get(fieldValue);
		} else {
			return fieldValue;
		}
	}

	private void updateIdField(Object entity, Object insertedEntity, Field idField, String idFieldName) throws IllegalAccessException {
		if (idField == null) {
			throw new FrameworkSystemException("Id field with name=" + idFieldName + " not found for class: " + entity.getClass());
		}
		Object idValue = idField.get(insertedEntity);
		idField.set(entity, idValue);
	}
}
