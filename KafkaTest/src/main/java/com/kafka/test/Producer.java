package com.kafka.test;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@State(Scope.Thread)
public class Producer {
	
	Properties props = null;
	List<String> data = null;
	KafkaProducer< String, String > producer = null;
	ObjectMapper mapper = null;
	
    
    public Producer() {
		buildProperties();
		producer = new KafkaProducer < > (props);
		mapper = new ObjectMapper().registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		data = new DataGenerator().generateData().stream().map(event -> {
			try {
				return mapper.writeValueAsString(event);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}
    
    private void buildProperties() {
    	props = new Properties();
    	props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    }
    
    
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
    public void pushData(Blackhole blackhole) {
		
    	for (String event : data) {
    		ProducerRecord< String, String > record = new ProducerRecord < String, String > (
                    "event-store", 0, UUID.randomUUID().toString(), event);
        	producer.send(record);
    	}
    	
    }
}
