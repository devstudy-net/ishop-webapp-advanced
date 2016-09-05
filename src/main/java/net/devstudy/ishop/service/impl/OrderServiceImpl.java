package net.devstudy.ishop.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import net.devstudy.ishop.entity.Account;
import net.devstudy.ishop.entity.Order;
import net.devstudy.ishop.entity.OrderItem;
import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.exception.AccessDeniedException;
import net.devstudy.ishop.exception.InternalServerErrorException;
import net.devstudy.ishop.exception.ResourceNotFoundException;
import net.devstudy.ishop.form.ProductForm;
import net.devstudy.ishop.model.CurrentAccount;
import net.devstudy.ishop.model.ShoppingCart;
import net.devstudy.ishop.model.ShoppingCartItem;
import net.devstudy.ishop.model.SocialAccount;
import net.devstudy.ishop.repository.AccountRepository;
import net.devstudy.ishop.repository.OrderItemRepository;
import net.devstudy.ishop.repository.OrderRepository;
import net.devstudy.ishop.repository.ProductRepository;
import net.devstudy.ishop.service.AvatarService;
import net.devstudy.ishop.service.CookieService;
import net.devstudy.ishop.service.NotificationContentBuilderService;
import net.devstudy.ishop.service.NotificationService;
import net.devstudy.ishop.service.OrderService;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Service
public class OrderServiceImpl implements OrderService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private AvatarService avatarService;
	
	@Autowired
	private CookieService cookieService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private NotificationContentBuilderService notificationContentBuilderService;
	
	@Override
	@Transactional(readOnly=true)
	public void addProductToShoppingCart(ProductForm productForm, ShoppingCart shoppingCart) {
		Product product = productRepository.findById(productForm.getIdProduct());
		if(product == null) {
			throw new InternalServerErrorException("Product not found by id="+productForm.getIdProduct());
		}
		shoppingCart.addProduct(product, productForm.getCount());
	}
	
	@Override
	public void removeProductFromShoppingCart(ProductForm form, ShoppingCart shoppingCart) {
		shoppingCart.removeProduct(form.getIdProduct(), form.getCount());
	}
	
	@Override
	public String serializeShoppingCart(ShoppingCart shoppingCart) {
		return cookieService.createShoppingCartCookie(shoppingCart.getItems());
	}

	@Override
	@Transactional(readOnly=true)
	public ShoppingCart deserializeShoppingCart(String cookieValue) {
		List<ProductForm> products = cookieService.parseShoppingCartCookie(cookieValue);
		ShoppingCart shoppingCart = new ShoppingCart();
		for(ProductForm productForm : products) {
			try {
				addProductToShoppingCart(productForm, shoppingCart);
			} catch (RuntimeException e) {
				LOGGER.error("Can't add product to ShoppingCart: productForm=" + productForm, e);
			}
		}
		return shoppingCart.getItems().isEmpty() ? null : shoppingCart;
	}
	
	@Override
	@Transactional
	public CurrentAccount authentificate(SocialAccount socialAccount) {
		Account account = accountRepository.findByEmail(socialAccount.getEmail());
		if (account == null) {
			String avatarUrl = avatarService.processAvatarLink(socialAccount.getAvatarUrl());
			account = new Account(socialAccount.getName(), socialAccount.getEmail(), avatarUrl);
			accountRepository.save(account);
		}
		return account;
	}
	
	@Override
	@Transactional
	public long makeOrder(ShoppingCart shoppingCart, final CurrentAccount currentAccount) {
		if (shoppingCart == null || shoppingCart.getItems().isEmpty()) {
			throw new InternalServerErrorException("shoppingCart is null or empty");
		}
		final Order order = new Order(currentAccount.getId(), new Timestamp(System.currentTimeMillis()));
		orderRepository.save(order);
		order.setItems(new ArrayList<>());
		for (ShoppingCartItem item : shoppingCart.getItems()) {
			OrderItem orderItem = new OrderItem(order.getId(), item.getProduct(), item.getCount());
			orderItemRepository.save(orderItem);
			order.getItems().add(orderItem);
		}
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				String content = notificationContentBuilderService.buildNewOrderCreatedNotificationMessage(order);
				notificationService.sendNotificationMessage(currentAccount.getEmail(), content);
			}
		});
		return order.getId();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Order findOrderById(long id, CurrentAccount currentAccount) {
		Order order = orderRepository.findById(id);
		if (order == null) {
			throw new ResourceNotFoundException("Order not found by id: " + id);
		}
		if (!order.getIdAccount().equals(currentAccount.getId())) {
			throw new AccessDeniedException("Account with id=" + currentAccount.getId() + " is not owner for order with id=" + id);
		}
		order.setItems(orderItemRepository.findByIdOrder(id));
		return order;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Order> listMyOrders(CurrentAccount currentAccount, int page, int limit) {
		return orderRepository.findByIdAccount(currentAccount.getId(), new PageRequest(page - 1, limit));
	}
	
	@Override
	@Transactional(readOnly=true)
	public int countMyOrders(CurrentAccount currentAccount) {
		return orderRepository.countByIdAccount(currentAccount.getId());
	}
}
