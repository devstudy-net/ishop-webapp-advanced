package net.devstudy.framework.annotation.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.devstudy.framework.SQLBuilder;
import net.devstudy.framework.handler.DefaultResultSetHandler;
import net.devstudy.framework.handler.ResultSetHandler;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SuppressWarnings("rawtypes")
public @interface Select {
	String value();

	Class<? extends ResultSetHandler> resultSetHandlerClass() default DefaultResultSetHandler.class;

	Class<? extends SQLBuilder> sqlBuilderClass() default SQLBuilder.class;
}
