package net.devstudy.framework.converter;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface Converter {
	<T> T convert(Class<T> entityClass, Object value);
}
