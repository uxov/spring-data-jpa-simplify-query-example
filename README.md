## Simplify Spring Data JPA Query Example
- Dynamic query
- Pagination and sorting
- Selecting specific columns  

## Requirements
JDK 1.8  
Gradle 5.5

## Code examples
```java
SelectQuery query = entityQuery.SelectQuery(User.class);
Condition c = query.getCondition();
Pageable page = PageRequest.of(pageNum - 1, pageSize, Sort.by("age").descending());
Page dataList = query.select("name", "age").where(
        c.greaterThan("age", 22),
        c.greaterThanOrEqualTo("age", 25)).getResult(page);
```

```java
SelectQuery query = entityQuery.SelectQuery(User.class);
Condition c = query.getCondition();
Pageable page = PageRequest.of(pageNum - 1, pageSize, Sort.by("age").descending());
query.select("name", "age").where(
        c.equal("sex", "female"));
if (age != null) {
    query.getWhere().add(c.greaterThan("age", age));
}
Page dataList = query.getResult().getResult(page);
```

```java
NormalQuery query = entityQuery.NormalQuery(User.class);
Condition c = query.getCondition();
Pageable page = PageRequest.of(pageNum - 1, pageSize, Sort.by("age").descending());
Page dataList = query.select().where(
        c.equal("sex", "male"),
        c.greaterThan("age", 22)).getResult(page);
```

```java
NormalQuery query = entityQuery.NormalQuery(User.class);
Condition c = query.getCondition();
Pageable page = PageRequest.of(pageNum - 1, pageSize, Sort.by("age").descending());
query.select().where(
        c.equal("sex", "female"));
if (age != null) {
    query.getWhere().add(c.greaterThan("age", age));
}
Page dataList = query.getResult().getResult(page);
```

## Usage
See in `src/test/java/xyz/defe/springDataJpa/test/SimplifyQueryTest.java`

