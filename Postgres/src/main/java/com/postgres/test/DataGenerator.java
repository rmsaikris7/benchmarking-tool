package com.postgres.test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.postgres.event.BankTransaction;
import com.postgres.event.Event;

public class DataGenerator {
	
	public List<Event> generateData(){
	
		List<Event> events = new ArrayList<Event>();
		try {
			for(int i = 0; i< 10000; i++) {
				events.add(eventBuilder(new BankTransaction(i, 123, "Credit", BigDecimal.valueOf(1000), LocalDateTime.now().minusDays(50000 - i)), i));
				
				for(int j=1; j<=19; j++ ) {
					
						events.add(eventBuilder(new BankTransaction(i, 123, "Debit", BigDecimal.valueOf(new Random().nextInt(51)), LocalDateTime.now().minusDays(50000 - i)), i));
					} 
				}
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		
		return events;
	} 
	private Event eventBuilder (BankTransaction t, int i) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		return new Event("transaction", t.getType() == "Credit" ? "AmountCredited" : "AmountDebited", t.getTransactionTime(), mapper.writeValueAsString(t));
	}
}
