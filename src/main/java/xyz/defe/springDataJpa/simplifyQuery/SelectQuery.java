package xyz.defe.springDataJpa.simplifyQuery;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class SelectQuery<T> {
	private Where where;

	private final Class<T> clasz;
	private final EntityManager em;
	private final CriteriaBuilder cb;

	private final CriteriaQuery query;
	private final Root<T> root;
	private final Condition condition;
	
	public SelectQuery(EntityManager em, Class<T> clasz) {
		this.em = em;
		cb = em.getCriteriaBuilder();
		this.clasz = clasz;
		query = cb.createTupleQuery();
		root = query.from(clasz);
		condition = new Condition(cb, root);
	}

	public Where select(String... fields) throws Exception {
		if (fields.length == 0) {
			throw new Exception("select method's arguments can not be empty!");
		}
		where = new Where(em, cb, query, root, Arrays.asList(fields));
		return where;
	}

	public Condition getCondition() {
		return condition;
	}

	public Where getWhere() throws Exception {
		if (where == null) {
			throw new Exception("Fields is empty,use select() to select fields!");
		}
		return where;
	}

	public Result getResult() throws Exception {
		if (where == null) {
			throw new Exception("Fields is empty,use select() to select fields!");
		}
		return where.getResult();
	}
}
