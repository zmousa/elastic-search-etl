Elastic Search ETL Project
==========================
This java project contains,
 * Camel ETL route from MySQL database into ElasticSearch as bulk insertion.
 * Elastic Search client, with searching restful API (match/fuzzy) search.

Camel Route:

```java
	from("jpa://com.es.model.Person?"
			+ "consumer.namedQuery=selectQuery&"
			+ "consumer.delay=2000&"
			+ "maxMessagesPerPoll=50&"
			+ "consumer.transacted=true&"
			+ "usePersist=true&"
			+ "consumeDelete=false")
	.threads(20)
	.process(docRowProcessor)
	.idempotentConsumer(header("id"), MemoryIdempotentRepository.memoryIdempotentRepository(100000))
	.to("seda:staging");

	from("seda:staging?"
			+ "multipleConsumers=true&"
			+ "concurrentConsumers=20")
		.aggregate(new ConstantExpression("false"), aggregateStrategy).ignoreInvalidCorrelationKeys().completionSize(50)
		.to("elasticsearch://elasticsearch?"
			+ "operation=BULK&"
			+ "indexName=person&"
			+ "indexType=person&"
			+ "ip=127.0.0.1&"
			+ "port=9300");
```



## Dependencies
 * [Apache Camel](http://camel.apache.org/) is an open source integration framework for ETL routes.
 * [Spring Framework](https://projects.spring.io/spring-framework) IOC container and manage application configurations.
 * [ElasticSearch](https://www.elastic.co/) distributed, full-text search engine
