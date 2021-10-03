package com.kafka.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.fasterxml.jackson.databind.ObjectMapper;


@State(Scope.Thread)
public class Consumer {
	
	Properties props = null;
	List<String> data = null;
	KafkaConsumer< String, String > consumer = null;
	ObjectMapper mapper = null;
	
    
    public Consumer() {
		buildProperties();
		consumer = new KafkaConsumer <> (props);
		consumer.subscribe(Collections.singleton("event-store"), new ConsumerRebalanceListener() {
			
			@Override
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {				
			}
			
			@Override
			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				consumer.seekToBeginning(partitions);
			}
		});
	}
    
    private void buildProperties() {
    	props = new Properties();
    	props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "benchmark-consumer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500000");
    }
    
    
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
    public void pushData(Blackhole blackhole) {
		while(true) {
			ConsumerRecords < String, String > consumerRecords = consumer.poll(1000);
			if(consumerRecords.count() == 0) {
				break;
			}
			
			consumer.commitAsync();
		}
    	
    }
}
