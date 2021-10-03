package com.axonserver.test;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

public class StartAxonBenchmark {

public static void main(String[] args) {
		
		try {
			Options opt = new OptionsBuilder()
					.forks(0)
	                .include(AxonDataWriter.class.getSimpleName())
	                .warmupIterations(0)
	                .verbosity(VerboseMode.NORMAL)
	                .mode(Mode.SingleShotTime)
	                 .build();

       
		new Runner(opt).run();
       } catch (RunnerException e) {
			e.printStackTrace();
       }
	}
}
