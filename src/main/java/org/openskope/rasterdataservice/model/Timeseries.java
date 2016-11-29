package org.openskope.rasterdataservice.model;

import java.util.Map;
import java.util.HashMap;

public class Timeseries {
    
    private Map<String, String[]> yearlyValues;

	public Timeseries() {
        yearlyValues = new HashMap<String,String[]>();
    }

    public Map<String, String[]> getData() {
        return this.yearlyValues;
    }

    public void put(String fileName, String[] values) {
        yearlyValues.put(fileName, values);
    }
}
