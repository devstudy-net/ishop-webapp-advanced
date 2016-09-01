package net.devstudy.ishop.service;

import java.util.Collection;
import java.util.List;

import net.devstudy.ishop.form.ProductForm;
import net.devstudy.ishop.model.ShoppingCartItem;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface CookieService {

	String createShoppingCartCookie(Collection<ShoppingCartItem> items);

	List<ProductForm> parseShoppingCartCookie(String cookieValue);
}
