package com.mongodb.test;

import java.util.List;

import org.bson.Document;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.postgres.event.Event;
import com.postgres.test.DataGenerator;

@State(Scope.Thread)
public class DataWriter {
	
	MongoDatabase db = null;
	MongoCollection<Document> collection = null;
	List<Event> events = null;
	
	public DataWriter() {
		this.db = new MongoClient("localhost", 27017).getDatabase("testDB");
		this.collection = db.getCollection("transactions");
		this.events = new DataGenerator().generateData();
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	public void insert() {
		int i = 0;
		for(Event event : events) {
			i++;
			Document document = new Document();
			document.append("id", i)
			.append("aggregate_id", event.getAggregateId())
			.append("event_type", event.getEventType())
			.append("created_at", event.getCreatedAt().toString())
			.append("data", Document.parse(event.getData()));
			collection.insertOne(document);
			document.clear();
		}
	}

}
