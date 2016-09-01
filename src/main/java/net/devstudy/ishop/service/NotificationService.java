package net.devstudy.ishop.service;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface NotificationService {

	void sendNotificationMessage(String notificationAddress, String content);
}
