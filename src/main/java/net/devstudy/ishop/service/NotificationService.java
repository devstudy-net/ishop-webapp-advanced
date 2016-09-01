package net.devstudy.ishop.service;

import net.devstudy.ishop.entity.Order;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface NotificationService {

	void sendNewOrderCreatedNotification(String notificationAddress, Order order);
}
