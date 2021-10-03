package com.esdb.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.eventstore.dbclient.AppendToStreamOptions;
import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.ExpectedRevision;
import com.eventstore.dbclient.ParseError;
import com.eventstore.dbclient.WriteResult;
import com.google.common.collect.Lists;

@State(Scope.Thread)
public class ESDBBatchInsertClient {
	
	
	private EventStoreDBClientSettings settings = null;
	private EventStoreDBClient client = null;
	private AppendToStreamOptions options = null;
	  
	private List<EventData[]> newlist = new ArrayList<EventData[]>();

	
	public ESDBBatchInsertClient() {
		try {
			this.settings = EventStoreDBConnectionString.parse("esdb://127.0.0.1:2113?tls=false");
		} catch (ParseError e) {
			e.printStackTrace();
		};
		this.client = EventStoreDBClient.create(settings);
		
		this.options = AppendToStreamOptions.get()
		        .expectedRevision(ExpectedRevision.ANY);
		this.operation();
	}
	
	public void operation() {
		int i = 0;
		List<EventData> eventList = new DataGenerator().generateData();
		
		for(List<EventData> eventData : Lists.partition(eventList, 5000)) {
			EventData[] eventBatch = new EventData[5000];
			eventData.toArray(eventBatch);
			newlist.add(eventBatch);
		}
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	public void insert(Blackhole blackhole) {
		try {
			for(EventData[] data : newlist) {
				WriteResult result = client.appendToStream("testing-stream", options, data).get();
				blackhole.consume(result);
			}
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
	}

}

