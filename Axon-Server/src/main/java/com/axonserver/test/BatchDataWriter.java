package com.axonserver.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.google.common.collect.Lists;

import io.axoniq.axonserver.connector.AxonServerConnection;
import io.axoniq.axonserver.connector.AxonServerConnectionFactory;
import io.axoniq.axonserver.connector.event.AppendEventsTransaction;
import io.axoniq.axonserver.connector.event.EventChannel;
import io.axoniq.axonserver.connector.impl.ServerAddress;
import io.axoniq.axonserver.grpc.event.Confirmation;
import io.axoniq.axonserver.grpc.event.Event;


@State(Scope.Thread)
public class BatchDataWriter {
	
	
	private AxonServerConnectionFactory client = null;
	private AxonServerConnection connection = null;
	private EventChannel channel = null;
	private List<Event> events = null;
	private List<AppendEventsTransaction> txs = null;
	
	public BatchDataWriter() {
		
		this.client = AxonServerConnectionFactory.forClient("tester")
				.routingServers(new ServerAddress("localhost", 8124))
				 .reconnectInterval(500, TimeUnit.MILLISECONDS)
                 .build();
		
		this.connection = client.connect("default");
		this.channel = connection.eventChannel();
		this.txs = new ArrayList<AppendEventsTransaction>();
		this.operation();
	}
	
	public void operation() {
		
		List<Event> events = new DataGenerator().generateData();
		
		for(List<Event> eventList :  Lists.partition(events, 32766)) {
			AppendEventsTransaction tx = channel.startAppendEventsTransaction();
			for(Event event : eventList) {
				tx.appendEvent(event);
			}
			txs.add(tx);
		}
		
	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	public void insert(Blackhole blackhole) {	
		
		try {
			for(AppendEventsTransaction tx : txs) {
				Confirmation confirm = tx.commit().get();
				blackhole.consume(confirm);
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
	}
}
