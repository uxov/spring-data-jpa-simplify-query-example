package xyz.defe.springDataJpa.simplifyQuery;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class NormalQuery<T> {
	private Where where;

	private final Class<T> clasz;
	private final EntityManager em;
	private final CriteriaBuilder cb;

	private final CriteriaQuery query;
	private final Root<T> root;
	private final Condition condition;
	
	public NormalQuery(EntityManager em, Class<T> clasz) {
		this.em = em;
		cb = em.getCriteriaBuilder();
		this.clasz = clasz;
		query = cb.createQuery(clasz);
		root = query.from(clasz);
		condition = new Condition(cb, root);
		where = new Where(em, cb, query, root, null);
	}

	public Where select() {
		return where;
	}
	
	public Condition getCondition() {
		return condition;
	}

	public Where getWhere() {
		return where;
	}

	public Result getResult() {
		return where.getResult();
	}
}
