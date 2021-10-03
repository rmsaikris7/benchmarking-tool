package com.esdb.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventDataBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.postgres.event.BankTransaction;

public class DataGenerator {

	private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
	
	public List<EventData> generateData(){
	
		List<EventData> events = new ArrayList<EventData>();
		try {
			for(int i = 0; i< 50000; i++) {
				events.add(eventBuilder(new BankTransaction(i, 123, "Credit", BigDecimal.valueOf(1000), LocalDateTime.now().minusDays(1000 - i))));
				
				for(int j=1; j<=19; j++ ) {
					
						events.add(eventBuilder(new BankTransaction(i, 123, "Debit", BigDecimal.valueOf(50), LocalDateTime.now().minusDays(1000 - i))));
					} 
				}
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		
		return events;
	} 
	
	private EventData eventBuilder (BankTransaction t) throws JsonProcessingException {
		EventData data = EventDataBuilder.binary(UUID.randomUUID(), 
				t.getType() == "Credit" ? "AmountCredited" : "AmountDebited",
						mapper.writeValueAsBytes(t)).build();
		return data;
	}
}
