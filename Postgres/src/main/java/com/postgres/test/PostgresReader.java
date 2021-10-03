package com.postgres.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
public class PostgresReader {

	private Connection conn = null;
	private PreparedStatement ps = null;
	private String insertQuery = "select * from events where aggregate_id = 'transaction'";
	
	public PostgresReader() {
		try {
			this.conn = DriverManager.getConnection("jdbc:postgresql://localhost/BankDB?user=postgres&password=admin");
			this.ps = conn.prepareStatement(insertQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	public void read (Blackhole blackhole) {
		try {
			
			ResultSet set = ps.executeQuery();
			blackhole.consume(set);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
