package com.postgres.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.postgres.event.Event;

@State(Scope.Thread)
public class PostgresWriter {
	
	private Connection conn = null;
	private PreparedStatement ps = null;
	private List<Event> events = null;
	private String insertQuery = "INSERT INTO events (aggregate_id, type, body, created_at) VALUES (?,?,?::JSON,?)";
	
	public PostgresWriter() {
		try {
			this.conn = DriverManager.getConnection("jdbc:postgresql://localhost/BankDB?user=postgres&password=admin");
			this.ps = conn.prepareStatement(insertQuery);
			this.events = new DataGenerator().generateData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	public void insert (Blackhole blackhole) {
		try {
			for(Event event: events) {
				ps.setString(1, event.getAggregateId());
				ps.setString(2,  event.getEventType());
				ps.setObject(3, event.getData());
				ps.setObject(4, event.getCreatedAt());
				
				boolean status = ps.execute();
				blackhole.consume(status);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
