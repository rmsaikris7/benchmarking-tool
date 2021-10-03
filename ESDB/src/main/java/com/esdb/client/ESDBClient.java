package com.esdb.client;

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

@State(Scope.Thread)
public class ESDBClient {

	
	private EventStoreDBClientSettings settings = null;
	private EventStoreDBClient client = null;
	private AppendToStreamOptions options = null;
	private List<EventData> data = null;
	

	
	public ESDBClient() {
		try {
			this.settings = EventStoreDBConnectionString.parse("esdb://127.0.0.1:2113?tls=false");
		} catch (ParseError e) {
			e.printStackTrace();
		};
		this.client = EventStoreDBClient.create(settings);
		this.options = AppendToStreamOptions.get()
		        .expectedRevision(ExpectedRevision.ANY);
		this.data = new DataGenerator().generateData();
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	public void insert(Blackhole blackhole) {
		data.forEach(e->{
			try {
				WriteResult result = client.appendToStream("transaction-stream", options, e).get();
				blackhole.consume(result);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
		});
		
	}

}
