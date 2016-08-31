package net.devstudy.ishop.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devstudy.framework.factory.DependencyInjectionManager;
import net.devstudy.ishop.service.OrderService;
import net.devstudy.ishop.service.ProductService;
import net.devstudy.ishop.service.SocialService;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class ServiceManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceManager.class);

	public static ServiceManager getInstance(ServletContext context) {
		ServiceManager instance = (ServiceManager) context.getAttribute("SERVICE_MANAGER");
		if (instance == null) {
			instance = new ServiceManager(context);
			context.setAttribute("SERVICE_MANAGER", instance);
		}
		return instance;
	}

	public ProductService getProductService() {
		return dependencyInjectionManager.getInstance(ProductService.class);
	}

	public OrderService getOrderService() {
		return dependencyInjectionManager.getInstance(OrderService.class);
	}

	public SocialService getSocialService() {
		return dependencyInjectionManager.getInstance(SocialService.class);
	}

	public String getApplicationProperty(String key) {
		String value = applicationProperties.getProperty(key);
		if (value.startsWith("${sysEnv.")) {
			String keyVal = value.replace("${sysEnv.", "").replace("}", "");
			value = System.getProperty(keyVal);
		}
		return value;
	}

	public void close() {
		try {
			dataSource.close();
		} catch (SQLException e) {
			LOGGER.error("Close datasource failed: " + e.getMessage(), e);
		}
		dependencyInjectionManager.destroyInstances();
	}

	final Properties applicationProperties = new Properties();
	final BasicDataSource dataSource;
	final DependencyInjectionManager dependencyInjectionManager;

	private ServiceManager(ServletContext context) {
		loadApplicationProperties();
		dataSource = createDataSource();
		
		Map<Class<?>, Object> externalDependencies = new HashMap<>();
		externalDependencies.put(DataSource.class, dataSource);
		dependencyInjectionManager = new DependencyInjectionManager(applicationProperties, externalDependencies);
		dependencyInjectionManager.scanPackage("net.devstudy.ishop.repository");
		dependencyInjectionManager.scanPackage("net.devstudy.ishop.service.impl");
		dependencyInjectionManager.injectDependencies();
	}

	private BasicDataSource createDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDefaultAutoCommit(false);
		dataSource.setRollbackOnReturn(true);
		dataSource.setDriverClassName(getApplicationProperty("db.driver"));
		dataSource.setUrl(getApplicationProperty("db.url"));
		dataSource.setUsername(getApplicationProperty("db.username"));
		dataSource.setPassword(getApplicationProperty("db.password"));
		dataSource.setInitialSize(Integer.parseInt(getApplicationProperty("db.pool.initSize")));
		dataSource.setMaxTotal(Integer.parseInt(getApplicationProperty("db.pool.maxSize")));
		return dataSource;
	}

	private void loadApplicationProperties() {
		try (InputStream in = ServiceManager.class.getClassLoader().getResourceAsStream("application.properties")) {
			applicationProperties.load(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
