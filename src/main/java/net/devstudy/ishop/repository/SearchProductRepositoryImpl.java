package net.devstudy.ishop.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.form.SearchForm;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@SuppressWarnings("unchecked")
public class SearchProductRepositoryImpl extends SimpleJpaRepository<Product, Integer> implements SearchProductRepository {
	private final EntityManager entityManager;
	public SearchProductRepositoryImpl(JpaEntityInformation<Product, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	public List<Product> listProductsBySearchForm(SearchForm searchForm, Pageable pageable) {
		StringBuilder hql = new StringBuilder("from Product p where (p.name like :query or p.description like :query)");
		Map<String, Object> params = populateSearchParams(searchForm, hql);
		Query query = entityManager.createQuery(hql.toString());
		setQueryParameters(query, params);
		query.setMaxResults(pageable.getPageSize());
		query.setFirstResult(pageable.getOffset());
		return query.getResultList();
	}

	@Override
	public int countProductsBySearchForm(SearchForm searchForm) {
		StringBuilder hql = new StringBuilder("select count(*) from Product p where (p.name like :query or p.description like :query)");
		Map<String, Object> params = populateSearchParams(searchForm, hql);
		Query query = entityManager.createQuery(hql.toString());
		setQueryParameters(query, params);
		return ((Number)query.getSingleResult()).intValue();
	}
	
	protected void setQueryParameters(Query query, Map<String, Object> params) {
		for(Map.Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}

	private Map<String, Object> populateSearchParams(SearchForm searchForm, StringBuilder hql) {
		Map<String, Object> params = new HashMap<>();
		params.put("query", "%"+searchForm.getQuery()+"%");
		if(!searchForm.getCategories().isEmpty()) {
			hql.append(" and p.category.id in (:categories) ");
			params.put("categories", searchForm.getCategories());
		}
		if(!searchForm.getProducers().isEmpty()) {
			hql.append(" and p.producer.id in (:producers) ");
			params.put("producers", searchForm.getProducers());
		}
		return params;
	}
}
