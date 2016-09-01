package net.devstudy.ishop.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devstudy.framework.annotation.Component;
import net.devstudy.framework.annotation.Value;
import net.devstudy.ishop.entity.Order;
import net.devstudy.ishop.service.NotificationService;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Component
public class AsyncEmailNotificationService implements NotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncEmailNotificationService.class);
	private final ExecutorService executorService;

	@Value("email.smtp.server")
	private String smtpHost;
	@Value("email.smtp.port")
	private String smtpPort;
	@Value("email.smtp.username")
	private String smtpUsername;
	@Value("email.smtp.password")
	private String smtpPassword;
	@Value("email.smtp.fromAddress")
	private String fromEmail;
	@Value("app.host")
	private String host;
	@Value("email.smtp.tryCount")
	private String tryCount;

	public AsyncEmailNotificationService() {
		executorService = Executors.newCachedThreadPool();
	}

	protected String buildNotificationMessage(Order order) {
		return host + "/order?id=" + order.getId();
	}

	@Override
	public void sendNewOrderCreatedNotification(String notificationAddress, Order order) {
		String content = buildNotificationMessage(order);
		executorService.submit(new EmailItem(notificationAddress, "New order", content, Integer.parseInt(tryCount)));
	}

	public void close() {
		executorService.shutdown();
	}

	private class EmailItem implements Runnable {
		private final String emailAddress;
		private final String subject;
		private final String content;
		private int tryCount;

		private EmailItem(String emailAddress, String subject, String content, int tryCount) {
			this.emailAddress = emailAddress;
			this.subject = subject;
			this.content = content;
			this.tryCount = tryCount;
		}

		private boolean isValidTryCount() {
			return tryCount > 0;
		}

		public void run() {
			try {
				SimpleEmail email = new SimpleEmail();
				email.setCharset("utf-8");
				email.setHostName(smtpHost);
				email.setSSLOnConnect(true);
				email.setSslSmtpPort(smtpPort);
				email.setAuthenticator(new DefaultAuthenticator(smtpUsername, smtpPassword));
				email.setFrom(fromEmail);
				email.setSubject(subject);
				email.setMsg(content);
				email.addTo(emailAddress);
				email.send();
			} catch (EmailException e) {
				LOGGER.error("Can't send email: " + e.getMessage(), e);
				tryCount--;
				if (isValidTryCount()) {
					LOGGER.info("Resend email: {}", this.toString());
					executorService.submit(this);
				} else {
					LOGGER.error("Email was not sent: limit of try count");
				}
			} catch (Exception e) {
				LOGGER.error("Error during send email: " + e.getMessage(), e);
			}
		}
	}
}
