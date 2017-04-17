package com.es.client;

import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchClient {

    private static final Logger LOG = LoggerFactory
	    .getLogger(ElasticsearchClient.class);
    
    public static final String indexName = "person";
    public static final String typeName = "person";
    
    private static ESClient esClient;
    private static ElasticsearchClient instance;
    
    public static ElasticsearchClient getInstance(){
    	if (instance == null) {
    		instance = new ElasticsearchClient();
    		esClient = new ESClient();
    	}
    	return instance;
    }
    
    public SearchResponse queryTerm(String field, String term){
    	QueryController queryController = new QueryController(esClient);
    	return queryController.queryTerm(indexName, typeName, field, term);
    }
    
    public SearchResponse queryFuzzy(String field, String term){
    	QueryController queryController = new QueryController(esClient);
    	return queryController.queryFuzzy(indexName, typeName, field, term);
    }
}
