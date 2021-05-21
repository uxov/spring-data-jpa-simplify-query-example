## Simplify Spring Data JPA Query Example
- Dynamic query
- Pagination and sorting
- Selecting specific columns  

## Requirements
- JDK version >= 8  
- Gradle

## Code Examples
### Select specific columns
```java
SelectQuery query = entityQuery.SelectQuery(User.class);
Condition c = query.getCondition();

Page dataList = query.select("name", "age").where(
	c.greaterThanOrEqualTo("age", 25)
).getResult(pageable);
```

### Select all columns
```java
NormalQuery query = entityQuery.NormalQuery(User.class);
Condition c = query.getCondition();

Page dataList = query.select().where(
	c.equal("sex", "male"),
	c.greaterThan("age", 22)
).getResult(pageable);
```

### Ignore null value condition(not need to check for null)
```java
NormalQuery query = entityQuery.NormalQuery(User.class);
Condition c = query.getCondition();

String sex = null;

Page dataList = query.select().where(
	c.equal("sex", sex),
	c.greaterThan("age", 22)
).getResult(pageable);
```

### Add condition
```java
query.getWhere().add(c.greaterThan("age", 22));
```

### In condition
```java
Set ageSet = new HashSet<>();
ageSet.add(22);
ageSet.add(27);

Page dataList = query.select().where(
	c.in("age", ageSet)
).getResult(pageable);
```

## Usage
See more detail in `src/test/java/xyz/defe/springDataJpa/test/SimplifyQueryTest.java`

