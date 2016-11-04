package org.openskope.service.locationinfo;

import java.util.Map;
import java.util.HashMap;

public class ConditionVsTimeResponse {
    
    private Map<String, String[]> yearlyValues;

	public ConditionVsTimeResponse() {
        yearlyValues = new HashMap<String,String[]>();
    }

    public Map<String, String[]> getData() {
        return this.yearlyValues;
    }

    public void put(String fileName, String[] values) {
        yearlyValues.put(fileName, values);
    }
}
