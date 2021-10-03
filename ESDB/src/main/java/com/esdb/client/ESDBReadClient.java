package com.esdb.client;

import java.util.concurrent.ExecutionException;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.ParseError;
import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;

@State(Scope.Thread)
public class ESDBReadClient {
	
	private EventStoreDBClientSettings settings = null;
	private EventStoreDBClient client = null;
	private ReadStreamOptions options = null;

	public ESDBReadClient() {
		try {
			this.settings = EventStoreDBConnectionString.parse("esdb://127.0.0.1:2113?tls=false");
		} catch (ParseError e) {
			e.printStackTrace();
		};
		this.client = EventStoreDBClient.create(settings);
		this.options = ReadStreamOptions.get()
		        .forwards()
		        .fromStart();
	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	public void read(Blackhole blackhole) {
		try {

			ReadResult result = client.readStream("testing-stream", options)
			        .get();
			System.out.println(result.getEvents().size());
			blackhole.consume(result);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}

