package xyz.defe.springDataJpa.simplifyQuery;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class Result {

	abstract long getCount();

	abstract <T> List<T> getData(Pageable page);

	public abstract <T> Page<T> getResult(Pageable page);

}