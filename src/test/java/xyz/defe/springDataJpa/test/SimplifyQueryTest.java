package xyz.defe.springDataJpa.test;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import xyz.defe.springDataJpa.entity.User;
import xyz.defe.springDataJpa.repository.UserRepository;
import xyz.defe.springDataJpa.simplifyQuery.Condition;
import xyz.defe.springDataJpa.simplifyQuery.EntityQuery;
import xyz.defe.springDataJpa.simplifyQuery.NormalQuery;
import xyz.defe.springDataJpa.simplifyQuery.SelectQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

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
	private Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("age").descending());

	@Test
	public void selectSpecificColumns() throws Exception {
		SelectQuery query = entityQuery.SelectQuery(User.class);
		Condition c = query.getCondition();

		Page dataList = query.select("name", "age").where(
				c.greaterThanOrEqualTo("age", 25)
		).getResult(pageable);

		Assertions.assertEquals(2, dataList.getTotalElements());
		System.out.println("selectSpecificColumns:json = " + gson.toJson(dataList.getContent()));
	}

	@Test
	public void selectAllColumns() {
		NormalQuery query = entityQuery.NormalQuery(User.class);
		Condition c = query.getCondition();

		Page dataList = query.select().where(
				c.equal("sex", "male"),
				c.greaterThan("age", 22)
		).getResult(pageable);

		Assertions.assertEquals(2, dataList.getTotalElements());
		System.out.println("selectAllColumns:json = " + gson.toJson(dataList.getContent()));
	}

	/**
	 * ignore null value condition(not need to check for null)
	 */
	@Test
	public void testNullValueCondition() {
		NormalQuery query = entityQuery.NormalQuery(User.class);
		Condition c = query.getCondition();

		String sex = null;
		Page dataList = query.select().where(
				c.equal("sex", sex),
				c.greaterThan("age", 22)
		).getResult(pageable);

		Assertions.assertEquals(4, dataList.getTotalElements());
		System.out.println("testNullValueCondition:json = " + gson.toJson(dataList.getContent()));
	}

	@Test
	public void addCondition() {
		NormalQuery query = entityQuery.NormalQuery(User.class);
		Condition c = query.getCondition();

		query.select().where(
				c.equal("sex", "female"));

		query.getWhere().add(c.greaterThan("age", 22));

		Page dataList = query.getResult().getResult(pageable);

		Assertions.assertEquals(2, dataList.getTotalElements());
		System.out.println("addCondition:json = " + gson.toJson(dataList.getContent()));
	}

	@Test
	public void testInCondition() {
		NormalQuery query = entityQuery.NormalQuery(User.class);
		Condition c = query.getCondition();

		Set ageSet = new HashSet<>();
		ageSet.add(22);
		ageSet.add(27);

		Page dataList = query.select().where(
				c.in("age", ageSet)
		).getResult(pageable);

		Assertions.assertEquals(2, dataList.getTotalElements());
		System.out.println("testInCondition:json = " + gson.toJson(dataList.getContent()));
	}
	
	@BeforeEach
	public void addUsers() {
		userRepo.deleteAll();

		User u1 = new User();
		u1.setName("Jhon");
		u1.setSex("male");
		u1.setAge(25);

		User u2 = new User();
		u2.setName("May");
		u2.setSex("female");
		u2.setAge(24);

		User u3 = new User();
		u3.setName("Mike");
		u3.setSex("male");
		u3.setAge(27);

		User u4 = new User();
		u4.setName("Helen");
		u4.setSex("female");
		u4.setAge(22);

		User u5 = new User();
		u5.setName("Anne");
		u5.setSex("female");
		u5.setAge(23);

		List list = new ArrayList();
		list.add(u1);
		list.add(u2);
		list.add(u3);
		list.add(u4);
		list.add(u5);
		userRepo.saveAll(list);

		List<User> data = userRepo.findAll();
		assertEquals(5, list.size());
	}
}
