package net.devstudy.ishop.util;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class SpringUtils {

	public static <T> T getInstance(ServletContext servletContext, Class<T> instanceClass) {
		WebApplicationContext context = (WebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		return context.getBean(instanceClass);
	}
}
