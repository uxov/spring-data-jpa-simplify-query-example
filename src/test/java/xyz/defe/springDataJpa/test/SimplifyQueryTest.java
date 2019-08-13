package xyz.defe.springDataJpa.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.google.gson.Gson;

import xyz.defe.springDataJpa.entity.User;
import xyz.defe.springDataJpa.repository.UserRepository;
import xyz.defe.springDataJpa.simplifyQuery.Condition;
import xyz.defe.springDataJpa.simplifyQuery.EntityQuery;
import xyz.defe.springDataJpa.simplifyQuery.NormalQuery;
import xyz.defe.springDataJpa.simplifyQuery.SelectQuery;

@SpringBootTest
public class SimplifyQueryTest {
	@Autowired
	private Gson gson;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private EntityQuery entityQuery;
	
	private int pageNum = 1;
	private int  pageSize = 3;
	
	@Test
	public void testSelectQuery() throws Exception {
		SelectQuery query = entityQuery.SelectQuery(User.class);
		Condition c = query.getCondition();
		Pageable page = PageRequest.of(pageNum - 1, pageSize, Sort.by("age").descending());
		Page dataList = query.select("name", "age").where(
				c.greaterThan("age", 22),
				c.greaterThanOrEqualTo("age", 25)).getResult(page);
		
		System.out.println("testSelectQuery()'s result:");
		System.out.println("count = " + dataList.getTotalElements());
		System.out.println("json = " + gson.toJson(dataList.getContent()));
		System.out.println();
	}
	
	@Test
	public void testSelectQuery2() throws Exception {
		Integer age = 22;
		
		SelectQuery query = entityQuery.SelectQuery(User.class);
		Condition c = query.getCondition();
		Pageable page = PageRequest.of(pageNum - 1, pageSize, Sort.by("age").descending());
		query.select("name", "age").where(
				c.equal("sex", "female"));
		if (age != null) {
			query.getWhere().add(c.greaterThan("age", age));
		}
		Page dataList = query.getResult().getResult(page);
		
		System.out.println("testSelectQuery2's result:");
		System.out.println("count = " + dataList.getTotalElements());
		System.out.println("json = " + gson.toJson(dataList.getContent()));
		System.out.println();
	}
	
	@Test
	public void testNormalQuery() {
		NormalQuery query = entityQuery.NormalQuery(User.class);
		Condition c = query.getCondition();
		Pageable page = PageRequest.of(pageNum - 1, pageSize, Sort.by("age").descending());
		Page dataList = query.select().where(
				c.equal("sex", "male"),
				c.greaterThan("age", 22)).getResult(page);
		
		System.out.println("testNormalQuery()'s result:");
		System.out.println("count = " + dataList.getTotalElements());
		System.out.println("json = " + gson.toJson(dataList.getContent()));
		System.out.println();
	}
	
	@Test
	public void testNormalQuery2() {
		Integer age = 22;
		
		NormalQuery query = entityQuery.NormalQuery(User.class);
		Condition c = query.getCondition();
		Pageable page = PageRequest.of(pageNum - 1, pageSize, Sort.by("age").descending());
		query.select().where(
				c.equal("sex", "female"));
		if (age != null) {
			query.getWhere().add(c.greaterThan("age", age));
		}
		Page dataList = query.getResult().getResult(page);
		
		System.out.println("testNormalQuery2()'s result:");
		System.out.println("count = " + dataList.getTotalElements());
		System.out.println("json = " + gson.toJson(dataList.getContent()));
		System.out.println();
	}
	
	@BeforeEach
	public void addUsers() {
		userRepo.deleteAll();
		
		User u1 = new User();
		u1.setName("Jhon");
		u1.setSex("male");
		u1.setAge(25);
		userRepo.save(u1);
		User u2 = new User();
		u2.setName("May");
		u2.setSex("female");
		u2.setAge(24);
		userRepo.save(u2);
		User u3 = new User();
		u3.setName("Mike");
		u3.setSex("male");
		u3.setAge(27);
		userRepo.save(u3);
		User u4 = new User();
		u4.setName("Helen");
		u4.setSex("female");
		u4.setAge(22);
		userRepo.save(u4);
		User u5 = new User();
		u5.setName("Anne");
		u5.setSex("female");
		u5.setAge(23);
		userRepo.save(u5);
		
		List<User> list = userRepo.findAll();
		assertEquals(5, list.size());
	}
}
