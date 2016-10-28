package org.skope.service.locationinfo.model;

import java.util.Map;
import java.util.HashMap;

public class LocationInfoResponse {
    
    private Map<String, String[]> yearlyValues;

	public LocationInfoResponse() {
        yearlyValues = new HashMap<String,String[]>();
    }

    public void setData(Map<String, String[]> yearlyValues) {
        this.yearlyValues = yearlyValues;
    }

    public Map<String, String[]> getData() {
        return this.yearlyValues;
    }

    public void put(String fileName, String[] values) {
        yearlyValues.put(fileName, values);
    }
}
