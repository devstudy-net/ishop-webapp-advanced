package net.devstudy.ishop.service.impl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.devstudy.ishop.entity.Order;
import net.devstudy.ishop.service.NotificationContentBuilderService;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Component
public class NotificationContentBuilderServiceImpl implements NotificationContentBuilderService {

	@Value("${app.host}")
	private String host;
	
	@Override
	public String buildNewOrderCreatedNotificationMessage(Order order) {
		return host + "/order?id=" + order.getId();
	}
}
