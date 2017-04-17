package com.es.client;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IndexController {
	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);
	
	private ESClient esClient;
	
	public IndexController(ESClient esClient) {
		this.esClient = esClient;
	}
	
	public void createIndex(String indexName, String type) {
		try {
	    	final CreateIndexRequestBuilder createIndexRequestBuilder = esClient.getClient().admin().indices().prepareCreate(indexName).setSettings(getSettings());
	        createIndexRequestBuilder.addMapping(type, getMapping(type));
	        createIndexRequestBuilder.execute().actionGet();
	        LOG.info("Index Created.");
    	} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private XContentBuilder getMapping(String type) throws IOException{
        XContentBuilder mappingBuilder = jsonBuilder().startObject().startObject(type).startObject("properties")
			        .startObject("firstname").field("type", "string").field("analyzer", "arabic_name_analyzer").endObject()
			        .startObject("lastname").field("type", "string").field("analyzer", "arabic_name_analyzer").endObject()
			        .startObject("age").field("type", "integer").endObject()
			        .startObject("description").field("type", "string").endObject()
			        .endObject().endObject().endObject();
		return mappingBuilder;
	}
	
	private XContentBuilder getSettings() throws IOException{
		XContentBuilder settingsBuilder = XContentFactory.jsonBuilder()
		        .startObject()
		            .startObject("analysis")
		                .startObject("filter")
		                    .startObject("english_stemmer")
		                        .field("type","stemmer")
		                        .field("language","english")
		                    .endObject()
		                    .startObject("english_keywords")
		                        .field("type","keyword_marker")
		                        .field("keywords","[]")
		                    .endObject()
		                    .startObject("english_stop")
		                        .field("type","stop")
		                        .field("stopwords","_english_")
		                    .endObject()
		                    .startObject("english_possessive_stemmer")
		                        .field("type","stemmer")
		                        .field("language","possessive_english")
		                    .endObject()
		                .endObject()
		                .startObject("char_filter")
		                    .startObject("strip_whitespace")
		                        .field("type","pattern_replace")
		                        .field("pattern","\\s")
		                        .field("replacement","")
		                    .endObject()
		                .endObject()
		                .startObject("analyzer")
		                    .startObject("english")
		                        .field("tokenizer","standard")
		                        .array("filter","english_possessive_stemmer","lowercase","english_stop","english_keywords","english_stemmer")
		                    .endObject()
		                .endObject()
		            .endObject()
		        .endObject();
		return settingsBuilder;
	}
}
