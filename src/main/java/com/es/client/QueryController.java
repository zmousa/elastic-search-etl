package com.es.client;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryController {
	private static final Logger LOG = LoggerFactory.getLogger(QueryController.class);
	
	private com.es.client.ESClient esClient;
	
	public QueryController(com.es.client.ESClient esClient) {
		this.esClient = esClient;
	}
	
	public SearchResponse queryFuzzy(String indexName, String type, String field, String value){
		SearchResponse response = esClient.getClient().prepareSearch(indexName)
		.setTypes(type)
		.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		//.setQuery(QueryBuilders.boolQuery().should(QueryBuilders.fuzzyQuery(field, value).fuzziness(Fuzziness.AUTO).prefixLength(0)))
		.setQuery(QueryBuilders.fuzzyQuery(field, value).fuzziness(Fuzziness.AUTO))
		//.setPostFilter(QueryBuilders.boolQuery().should(QueryBuilders.boolQuery().must(QueryBuilders.andQuery(QueryBuilders.termQuery("country", ""), QueryBuilders.termQuery("gender", ""))))) 
		.setFrom(0).setSize(60).setExplain(true)
		.execute()
		.actionGet();
		LOG.info("Response: " + response.toString());
		return response;
	}
	
	public SearchResponse queryTerm(String indexName, String type, String field, String value){
		SearchResponse response = esClient.getClient().prepareSearch(indexName)
		.setTypes(type)
		.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		.setQuery(QueryBuilders.termQuery(field, value))
		//.setPostFilter(QueryBuilders.boolQuery().should(QueryBuilders.boolQuery().must(QueryBuilders.andQuery(QueryBuilders.termQuery("country", ""), QueryBuilders.termQuery("gender", ""))))) 
		.setFrom(0).setSize(60).setExplain(true)
		.execute()
		.actionGet();
		LOG.info("Response: " + response.toString());
		return response;
	}
}
