package net.devstudy.ishop.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import net.devstudy.ishop.entity.Producer;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface ProducerRepository extends Repository<Producer, Integer>{
	
	List<Producer> findAll(Sort sort);
}
