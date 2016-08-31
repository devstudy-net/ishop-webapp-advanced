package net.devstudy.ishop.repository;

import java.util.List;

import net.devstudy.framework.annotation.jdbc.CollectionItem;
import net.devstudy.framework.annotation.jdbc.JDBCRepository;
import net.devstudy.framework.annotation.jdbc.Select;
import net.devstudy.ishop.entity.Producer;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@JDBCRepository
public interface ProducerRepository {

	@Select("select * from producer order by name")
	@CollectionItem(Producer.class)
	List<Producer> listAllProducers();
}
