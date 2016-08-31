package net.devstudy.framework.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.devstudy.framework.annotation.jdbc.Insert;
import net.devstudy.framework.annotation.jdbc.Select;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
final class JDBCRepositoryFactory {

	@SuppressWarnings("unchecked")
	static <T> T createRepository(Class<T> interfaceClass) {
		return (T) Proxy.newProxyInstance(JDBCRepositoryFactory.class.getClassLoader(), new Class[] { interfaceClass },
				new JDBCRepositoryInvocationHandler(interfaceClass));
	}

	private static class JDBCRepositoryInvocationHandler implements InvocationHandler {
		private JDBCSelectHelper selectSQLHelper = new JDBCSelectHelper();
		private JDBCInsertHelper insertSQLHelper = new JDBCInsertHelper();
		private Class<?> interfaceClass;

		private JDBCRepositoryInvocationHandler(Class<?> interfaceClass) {
			this.interfaceClass = interfaceClass;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				Select select = method.getAnnotation(Select.class);
				if (select != null) {
					return selectSQLHelper.select(select, method, args);
				}
				Insert insert = method.getAnnotation(Insert.class);
				if (insert != null) {
					return insertSQLHelper.insert(insert, method, args);
				}
				if ("toString".equals(method.getName())) {
					return "Proxy for " + interfaceClass + " class";
				}
				throw new UnsupportedOperationException("Can't execute method: " + method);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}
	}

}
