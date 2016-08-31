package net.devstudy.ishop.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devstudy.framework.annotation.jdbc.Transactional;
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
import net.devstudy.ishop.service.OrderService;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class OrderServiceImpl implements OrderService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
	private final AccountRepository accountRepository;
	private final OrderItemRepository orderItemRepository;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	private final String rootDir;

	private String smtpHost;
	private String smtpPort;
	private String smtpUsername;
	private String smtpPassword;
	private String host;
	private String fromAddress;

	public OrderServiceImpl(ServiceManager serviceManager) {
		super();
		this.rootDir = serviceManager.getApplicationProperty("app.avatar.root.dir");

		this.smtpHost = serviceManager.getApplicationProperty("email.smtp.server");
		this.smtpPort = serviceManager.getApplicationProperty("email.smtp.port");
		this.smtpUsername = serviceManager.getApplicationProperty("email.smtp.username");
		this.smtpPassword = serviceManager.getApplicationProperty("email.smtp.password");
		this.host = serviceManager.getApplicationProperty("app.host");
		this.fromAddress = serviceManager.getApplicationProperty("email.smtp.fromAddress");
		
		this.accountRepository = serviceManager.accountRepository;
		this.orderItemRepository = serviceManager.orderItemRepository;
		this.orderRepository = serviceManager.orderRepository;
		this.productRepository = serviceManager.productRepository;
	}

	@Override
	@Transactional
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
		StringBuilder res = new StringBuilder();
		for (ShoppingCartItem item : shoppingCart.getItems()) {
			res.append(item.getProduct().getId()).append("-").append(item.getCount()).append("|");
		}
		if (res.length() > 0) {
			res.deleteCharAt(res.length() - 1);
		}
		return res.toString();
	}

	@Override
	@Transactional
	public ShoppingCart deserializeShoppingCart(String string) {
		ShoppingCart shoppingCart = new ShoppingCart();
		String[] items = string.split("\\|");
		for (String item : items) {
			try {
				String data[] = item.split("-");
				int idProduct = Integer.parseInt(data[0]);
				int count = Integer.parseInt(data[1]);
				addProductToShoppingCart(new ProductForm(idProduct, count), shoppingCart);
			} catch (RuntimeException e) {
				LOGGER.error("Can't add product to ShoppingCart during deserialization: item=" + item, e);
			}
		}
		return shoppingCart.getItems().isEmpty() ? null : shoppingCart;
	}

	@Override
	@Transactional(readOnly=false)
	public CurrentAccount authentificate(SocialAccount socialAccount) {
		try{
			Account account = accountRepository.findByEmail(socialAccount.getEmail());
			if (account == null) {
				String uniqFileName = UUID.randomUUID().toString() + ".jpg";
				Path filePathToSave = Paths.get(rootDir + "/" + uniqFileName);
				downloadAvatar(socialAccount.getAvatarUrl(), filePathToSave);
				account = new Account(socialAccount.getName(), socialAccount.getEmail(), "/media/avatar/" + uniqFileName);
				accountRepository.create(account);
			}
			return account;
		} catch (IOException e) {
			throw new InternalServerErrorException("Can't process avatar link", e);
		}
	}

	protected void downloadAvatar(String avatarUrl, Path filePathToSave) throws IOException {
		try (InputStream in = new URL(avatarUrl).openStream()) {
			Files.copy(in, filePathToSave);
		}
	}

	@Override
	@Transactional(readOnly=false)
	public long makeOrder(ShoppingCart shoppingCart, CurrentAccount currentAccount) {
		validateShoppingCart(shoppingCart);
		Order order = new Order(currentAccount.getId(), new Timestamp(System.currentTimeMillis()));
		orderRepository.create(order);
		for (ShoppingCartItem item : shoppingCart.getItems()) {
			orderItemRepository.create(new OrderItem(order.getId(), item.getProduct(), item.getCount()));
		}
		sendEmail(currentAccount.getEmail(), order);
		return order.getId();
	}
	
	private void validateShoppingCart (ShoppingCart shoppingCart) {
		if (shoppingCart == null || shoppingCart.getItems().isEmpty()) {
			throw new InternalServerErrorException("shoppingCart is null or empty");
		}
	}

	private void sendEmail(String emailAddress, Order order) {
		try {
			SimpleEmail email = new SimpleEmail();
			email.setCharset("utf-8");
			email.setHostName(smtpHost);
			email.setSSLOnConnect(true);
			email.setSslSmtpPort(smtpPort);
			email.setFrom(fromAddress);
			email.setAuthenticator(new DefaultAuthenticator(smtpUsername, smtpPassword));
			email.setSubject("New order");
			email.setMsg(host + "/order?id=" + order.getId());
			email.addTo(emailAddress);
			email.send();
		} catch (Exception e) {
			LOGGER.error("Error during send email: " + e.getMessage(), e);
		}
	}

	@Override
	@Transactional
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
	@Transactional
	public List<Order> listMyOrders(CurrentAccount currentAccount, int page, int limit) {
		int offset = (page - 1) * limit;
		return orderRepository.listMyOrders(currentAccount.getId(), offset, limit);
	}

	@Override
	@Transactional
	public int countMyOrders(CurrentAccount currentAccount) {
		return orderRepository.countMyOrders(currentAccount.getId());
	}
}
