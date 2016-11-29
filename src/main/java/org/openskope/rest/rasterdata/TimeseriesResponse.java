package org.openskope.rest.rasterdata;

import java.util.Map;
import java.util.HashMap;

public class TimeseriesResponse {
    
    private Map<String, String[]> yearlyValues;

	public TimeseriesResponse() {
        yearlyValues = new HashMap<String,String[]>();
    }

    public Map<String, String[]> getData() {
        return this.yearlyValues;
    }

    public void put(String fileName, String[] values) {
        yearlyValues.put(fileName, values);
    }
}
