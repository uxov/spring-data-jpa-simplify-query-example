package xyz.defe.springDataJpa.simplifyQuery;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.predicate.ComparisonPredicate;
import org.hibernate.query.criteria.internal.predicate.ComparisonPredicate.ComparisonOperator;
import org.hibernate.query.criteria.internal.predicate.LikePredicate;

import java.util.Set;

public class Condition {
	private final Root root;
	private final CriteriaBuilder cb;

	public Condition(CriteriaBuilder cb, Root root) {
		this.cb =cb;
		this.root = root;
	}
	
	Predicate addComparisonPredicate(ComparisonOperator op, String field, Object value) {
		if (value == null || value.equals("")) {
			return null;
		}
		return new ComparisonPredicate((CriteriaBuilderImpl) cb, op, root.get(field), value);
	}

	Predicate addLikePredicate(String field, String value) {
		if (value == null || value.equals("")) {
			return null;
		}
		return new LikePredicate((CriteriaBuilderImpl) cb, root.get(field), "%" + value + "%");
	}

	public Predicate equal(String field, Object value) {
		return addComparisonPredicate(ComparisonOperator.EQUAL, field, value);
	}

	public Predicate notEqual(String field, Object value) {
		return addComparisonPredicate(ComparisonOperator.NOT_EQUAL, field, value);
	}

	public Predicate greaterThan(String field, Object value) {
		return addComparisonPredicate(ComparisonOperator.GREATER_THAN, field, value);
	}

	public Predicate greaterThanOrEqualTo(String field, Object value) {
		return addComparisonPredicate(ComparisonOperator.GREATER_THAN_OR_EQUAL, field, value);
	}

	public Predicate lessThan(String field, Object value) {
		return addComparisonPredicate(ComparisonOperator.LESS_THAN, field, value);
	}

	public Predicate lessThanOrEqualTo(String field, Object value) {
		return addComparisonPredicate(ComparisonOperator.LESS_THAN_OR_EQUAL, field, value);
	}

	public Predicate like(String field, String value) {
		return addLikePredicate(field, value);
	}

	public Predicate notLike(String field, String value) {
		return addLikePredicate(field, value);
	}

	public Predicate between(String field, Object value1, Object value2) {
		if (value1 == null || value1.equals("") || value2 == null || value2.equals("")) {
			return null;
		}
		return cb.between(root.get(field), (Comparable) value1, (Comparable) value2);
	}
	
	public Predicate or(Predicate... predicates) {
		return cb.or(predicates);
	}
	
	public Predicate and(Predicate... predicates) {
		return cb.and(predicates);
	}

	public Predicate in(String field, Set objectSet) {
		if (null == objectSet || objectSet.isEmpty()) {return null;}
		objectSet.removeIf(o -> null == o || "".equals(o));
		return root.get(field).in(objectSet);
	}

}
