package com.es.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.es.client.ElasticsearchClient;

@Controller
@RequestMapping("/es")
public class RestController {

	static final Logger logger = Logger.getLogger(RestController.class);

	@RequestMapping(value = "/searchTerm", method = RequestMethod.GET)
	public @ResponseBody
	String searchTerm(@RequestParam String field, @RequestParam String term) {
		return ElasticsearchClient.getInstance().queryTerm(field, term).toString();

	}
	
	@RequestMapping(value = "/searchFuzzy", method = RequestMethod.GET)
	public @ResponseBody
	String searchFuzzy(@RequestParam String field, @RequestParam String term) {
		return ElasticsearchClient.getInstance().queryFuzzy(field, term).toString();
	}
}
