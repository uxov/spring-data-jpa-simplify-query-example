package xyz.defe.springDataJpa.simplifyQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
public class EntityQuery {
	
	@PersistenceContext
	private EntityManager em;
	
	public <T> NormalQuery<T> NormalQuery(Class<T> clasz) {
		NormalQuery<T> query = new NormalQuery<T>(em, clasz);
		return query;
	}
	
	public <T> SelectQuery<T> SelectQuery(Class<T> clasz) {
		SelectQuery<T> query = new SelectQuery<T>(em, clasz);
		return query;
	}
}
