package xyz.defe.springDataJpa.simplifyQuery;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

public class NormalResult extends Result {
	private final Root root;
	private final EntityManager em;
	private final CriteriaBuilder cb;
	private final CriteriaQuery query;

	private final List<Predicate> predicateList;

	public NormalResult(EntityManager em, CriteriaBuilder cb, CriteriaQuery query, Root root,
			List<Predicate> predicateList) {
		this.em = em;
		this.cb = cb;
		this.root = root;
		this.query = query;
		this.predicateList = predicateList;
	}

	@Override
	long getCount() {
		query.select(cb.count(root)).where(predicateList.toArray(new Predicate[predicateList.size()]));
		TypedQuery<Long> ctq = em.createQuery(query);
		Long count = ctq.getSingleResult();
		return count == null ? 0 : count;
	}

	@Override
	<T> List<T> getData(Pageable page) {
		query.select(root);
		query.where(predicateList.toArray(new Predicate[predicateList.size()]));
		query.orderBy(QueryUtils.toOrders(page.getSort(), root, cb));
		TypedQuery<T> typeTuery = em.createQuery(query);
		typeTuery.setFirstResult((int) page.getOffset());
		typeTuery.setMaxResults(page.getPageSize());
		return typeTuery.getResultList();
	}

	@Override
	public <T> Page<T> getResult(Pageable page) {
		long count = getCount();
		List<T> dataList = getData(page);
		Page<T> result = new PageImpl<T>(dataList, page, count);
		return result;
	}
}
