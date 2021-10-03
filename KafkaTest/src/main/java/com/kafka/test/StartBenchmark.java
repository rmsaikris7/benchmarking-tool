package com.kafka.test;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class StartBenchmark {

	public static void main(String[] args) {

		try {
			Options opt = new OptionsBuilder()
					.forks(0)
	                .include(Consumer.class.getSimpleName())
	                .warmupIterations(0)
	                .mode(Mode.SingleShotTime)
	                 .build();

       
		new Runner(opt).run();
       } catch (RunnerException e) {
			e.printStackTrace();
       }
	}

}
