package net.devstudy.ishop.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.devstudy.ishop.Constants;
import net.devstudy.ishop.service.ProductService;
import net.devstudy.ishop.util.SpringUtils;

/**
 * Example !!!! This filter is not working, because @WebFilter annotation is absent
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class CategoryProducerFilter extends AbstractFilter {

	private ProductService productService;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		productService = SpringUtils.getInstance(filterConfig.getServletContext(), ProductService.class);
	}
	
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setAttribute(Constants.CATEGORY_LIST, productService.listAllCategories());
		request.setAttribute(Constants.PRODUCER_LIST, productService.listAllProducers());
		chain.doFilter(request, response);
	}
}
