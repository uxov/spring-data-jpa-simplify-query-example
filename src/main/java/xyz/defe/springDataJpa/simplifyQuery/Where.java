package xyz.defe.springDataJpa.simplifyQuery;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class Where {
	private final Root root;
	private final EntityManager em;
	private final CriteriaBuilder cb;
	private final CriteriaQuery query;
	private final List<String> fieldLit;
	private final List<Predicate> predicateList = new ArrayList<>();

	private Result result;

	public Where(EntityManager em, CriteriaBuilder cb, CriteriaQuery<Tuple> query, Root root, List<String> fieldLit) {
		this.em = em;
		this.cb = cb;
		this.root = root;
		this.query = query;
		this.fieldLit = fieldLit;
	}

	public Result where(Predicate... predicates) {
		for (Predicate p : predicates) {
			if (p != null) {predicateList.add(p);}
		}
		if (fieldLit != null) {
			result = new SelectResult(em, cb, query, root, fieldLit, predicateList);
		} else {
			result = new NormalResult(em, cb, query, root, predicateList);
		}
		return result;
	}

	public Result add(Predicate... predicates) {
		for (Predicate p : predicates) {
			if (p != null) {predicateList.add(p);}
			predicateList.add(p);
		}
		return result;
	}

	public Result getResult() {
		if (result == null) {
			if (fieldLit != null) {
				result = new SelectResult(em, cb, query, root, fieldLit, predicateList);
			} else {
				result = new NormalResult(em, cb, query, root, predicateList);
			}
		}
		return result;
	}
}
