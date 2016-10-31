package org.openskope.util;

import java.io.IOException;
import java.io.InputStream;

public class StreamSink implements Runnable {
	
	private final InputStream _inputStream;
	private String capturedData = "";
	
	public StreamSink(InputStream inputStream) {
		_inputStream = inputStream;
	}

	public void run() {
		
	    StringBuffer buffer = new StringBuffer();
	    byte[] byteArray = new byte[4096];
	    int numBytesRead;
	    
		try {

			while ((numBytesRead = _inputStream.read(byteArray)) != -1) {
		        buffer.append(new String(byteArray, 0, numBytesRead));
		    }
		    
		    _inputStream.close();
		    
		} catch (IOException e) {
			throw new RuntimeException(
					"IOException receiving data from child process.");
		}

		capturedData = buffer.toString();
	}

	public String toString() { 
		return capturedData; 
	}
}

