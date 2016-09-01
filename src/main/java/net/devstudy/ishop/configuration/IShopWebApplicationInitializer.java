package net.devstudy.ishop.configuration;

import java.util.EnumSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import net.devstudy.ishop.listener.IShopApplicationListener;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class IShopWebApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) throws ServletException {
		WebApplicationContext ctx = createWebApplicationContext(container);

		container.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
		container.addListener(new ContextLoaderListener(ctx));
		container.addListener(new IShopApplicationListener());
	}

	private WebApplicationContext createWebApplicationContext(ServletContext container) {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.scan("net.devstudy.ishop.configuration");
		ctx.setServletContext(container);
		ctx.refresh();
		return ctx;
	}
}
