package com.es.camel;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.es.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DocRowProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Person person = exchange.getIn().getBody(Person.class);
		Map<String, Object> personJson = new HashMap<String, Object>();
		personJson = getObjectMapper(person);
		Map<String,Object> headers = new HashMap<String, Object>();
		headers.put("id", person.getId());
		exchange.getOut().setHeaders(headers);
        exchange.getOut().setBody(personJson);
	}
	
	private Map<String, Object> getObjectMapper(Person person) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(person);
			
			map = mapper.readValue(jsonInString, new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
