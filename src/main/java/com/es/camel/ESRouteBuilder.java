package com.es.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.language.ConstantExpression;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ESRouteBuilder extends RouteBuilder {
	
	@Autowired
	DocRowProcessor docRowProcessor;
	
	@Autowired
	DocAggregator aggregateStrategy;
	
	public void configure() {
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
    }
}