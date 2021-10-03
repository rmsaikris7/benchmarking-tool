package com.axonserver.test;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import io.axoniq.axonserver.connector.AxonServerConnection;
import io.axoniq.axonserver.connector.AxonServerConnectionFactory;
import io.axoniq.axonserver.connector.event.EventChannel;
import io.axoniq.axonserver.connector.impl.ServerAddress;
import io.axoniq.axonserver.grpc.event.Event;


@State(Scope.Thread)
public class AxonReadClient {
	
	
	private AxonServerConnectionFactory client = null;
	private AxonServerConnection connection = null;
	private EventChannel channel = null;
	
	
	public AxonReadClient() {
		
		this.client = AxonServerConnectionFactory.forClient("tester")
				.routingServers(new ServerAddress("localhost", 8124))
				 .reconnectInterval(500, TimeUnit.MILLISECONDS)
                 .build();
		
		this.connection = client.connect("default");
		this.channel = connection.eventChannel();

	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	public void insert(Blackhole blackhole) {	
		
		Stream<Event> stream = channel.openAggregateStream("bank_transactions").asStream();
		blackhole.consume(stream);
	}
}
