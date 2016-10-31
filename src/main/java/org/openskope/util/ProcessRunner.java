package org.openskope.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class ProcessRunner {

	public static StreamSink[] run(String cmdLine, String stdIn, String[] env, File workingDirectory) throws IOException, InterruptedException {
		
		// start the external process using the provided command line string
		Process process = Runtime.getRuntime().exec(cmdLine, env, workingDirectory);

		// send the provided standard input data to the stdin stream of the external process
		PrintStream stdinStream = new PrintStream(process.getOutputStream(), true);
		stdinStream.print(stdIn);
		stdinStream.close();
		
		// create a sink for the process stdout stream
		InputStream stdoutStream = process.getInputStream();
		StreamSink stdoutSink = new StreamSink(stdoutStream);

		// create a sink for the process stderr stream
		InputStream stderrStream = process.getErrorStream();		
		StreamSink stderrSink = new StreamSink(stderrStream);
		
		// create threads to read the stdin and stderr streams and capture them to the sinks
		Thread p1 = new Thread(stdoutSink);
		Thread p2 = new Thread(stderrSink);
		
		// start the two threads
		p1.start();
		p2.start();
		
		// wait for the external process to complete
		process.waitFor();
		
		// wait for the two output stream reading threads to complete
		p1.join();
		p2.join();
		
		// return the two stream sinks as an array
		return new StreamSink[] { stdoutSink, stderrSink };
	}
}
