package com.axonserver.test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.protobuf.ByteString;
import com.postgres.event.BankTransaction;
import com.thoughtworks.xstream.XStream;

import io.axoniq.axonserver.grpc.SerializedObject;
import io.axoniq.axonserver.grpc.event.Event;

public class DataGenerator {

	XStream serializer = null;
	private static int sequence_number = 0;
	
	public DataGenerator() {
		serializer = new XStream();
	}
	public List<Event> generateData(){
		
		List<Event> events = new ArrayList<Event>();
		for(int i = 0; i< 10000; i++) {
			events.add(eventBuilder(new BankTransaction(i, 123, "Credit", BigDecimal.valueOf(1000), LocalDateTime.now().minusDays(1000 - i))));
			
			for(int j=1; j<=19; j++ ) {
				
					events.add(eventBuilder(new BankTransaction(i, 123, "Debit", BigDecimal.valueOf(50), LocalDateTime.now().minusDays(1000 - i))));
				} 
			}
		
		
		return events;
	}
	
	private Event eventBuilder(BankTransaction payload) {
        return Event.newBuilder().setPayload(SerializedObject.newBuilder()
                                                             .setData(ByteString.copyFromUtf8((serialize(payload))))
                                                             .setType(BankTransaction.class.getCanonicalName()))
                    .setMessageIdentifier(UUID.randomUUID().toString())
                    .setAggregateIdentifier("bank_transactions")
                    .setAggregateSequenceNumber(sequence_number++)
                    .setAggregateType(payload.getType())
                    .setTimestamp(System.currentTimeMillis())
                    .build();
    }
	
	private String serialize(BankTransaction payload) {
		 return serializer.toXML(payload);
	}

}
