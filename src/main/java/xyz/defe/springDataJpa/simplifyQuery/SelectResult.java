package xyz.defe.springDataJpa.simplifyQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

public class SelectResult extends Result {
	private final Root root;
	private final EntityManager em;
	private final CriteriaBuilder cb;
	private final CriteriaQuery query;

	private final List<String> fieldList;
	private final List<Predicate> predicateList;

	public SelectResult(EntityManager em, CriteriaBuilder cb, CriteriaQuery query, Root root, List<String> fieldList,
			List<Predicate> predicateList) {
		this.em = em;
		this.cb = cb;
		this.root = root;
		this.query = query;
		this.fieldList = fieldList;
		this.predicateList = predicateList;
	}

	@Override
	long getCount() {
		query.select(cb.tuple(cb.count(root))).where(predicateList.toArray(new Predicate[predicateList.size()]));
		TypedQuery<Tuple> ctq = em.createQuery(query);
		Long count = ctq.getResultList().get(0).get(0, Long.class);
		return count == null ? 0 : count;
	}

	@Override
	List<Tuple> getData(Pageable page) {
		List<Selection> selectList = new ArrayList();

		fieldList.forEach(f -> selectList.add(root.get(f)));

		if (selectList.size() == 0) {
			query.multiselect(root);
		} else {
			query.multiselect(selectList.toArray(new Selection[selectList.size()]));
		}
		query.where(predicateList.toArray(new Predicate[predicateList.size()]));
		query.orderBy(QueryUtils.toOrders(page.getSort(), root, cb));

		TypedQuery<Tuple> typeTuery = em.createQuery(query);
		typeTuery.setFirstResult((int) page.getOffset());
		typeTuery.setMaxResults(page.getPageSize());
		List<Tuple> list = typeTuery.getResultList();

		return list;
	}

	@Override
	public Page<List<Map<String, Object>>> getResult(Pageable page) {
		List<Map<String, Object>> resultList = new ArrayList();

		long count = getCount();
		List<Tuple> dataList = getData(page);
		dataList.forEach(t -> {
			Map<String, Object> map = new HashMap();
			for (int i = 0; i < fieldList.size(); i++) {
				if (t.get(i) == null) {
					map.put(fieldList.get(i), "");
				} else {
					map.put(fieldList.get(i), t.get(i));
				}
			}
			resultList.add(map);
		});

		Page<List<Map<String, Object>>> result = new PageImpl(resultList, page, count);
		return result;
	}

}
