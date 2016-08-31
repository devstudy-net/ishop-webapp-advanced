package net.devstudy.ishop.repository.impl;

import java.util.List;

import net.devstudy.framework.factory.JDBCConnectionUtils;
import net.devstudy.framework.handler.DefaultListResultSetHandler;
import net.devstudy.framework.handler.ResultSetHandler;
import net.devstudy.ishop.entity.Producer;
import net.devstudy.ishop.jdbc.JDBCUtils;
import net.devstudy.ishop.repository.ProducerRepository;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class ProducerRepositoryImpl implements ProducerRepository {
	private final ResultSetHandler<List<Producer>> producerListResultSetHandler = new DefaultListResultSetHandler<>(Producer.class);
	
	@Override
	public List<Producer> listAllProducers() {
		return JDBCUtils.select(JDBCConnectionUtils.getCurrentConnection(), "select * from producer order by name", producerListResultSetHandler);
	}
}
