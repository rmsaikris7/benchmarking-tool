package com.mongodb.test;

import org.bson.Document;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@State(Scope.Thread)
public class DataReader {

	MongoDatabase db = null;
	MongoCollection<Document> collection = null;
	
	public DataReader() {
		this.db = new MongoClient("localhost", 27017).getDatabase("testDB");
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	public void read() {
		db.getCollection("transactions")
		.find(new BasicDBObject("aggregate_id","transaction"));
	}
}
