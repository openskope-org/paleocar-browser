package org.openskope.rasterdataservice.service;

import org.openskope.rasterdataservice.model.Timeseries;

import org.yesworkflow.util.exec.ProcessRunner;
import org.yesworkflow.util.exec.StreamSink;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;
import java.util.HashMap;


@Service
public class TimeseriesTableService implements InitializingBean {

    private final static String files[];
    
    static {
        files = new String[] {
            "GDD_may_sept_demosaic.tif",
            "PPT_annual_demosaic.tif",
            "PPT_may_sept_demosaic.tif",
            "PPT_water_year.tif"
        };
    };

	@Value("${raster-data-service.data-dir}") public String rasterDataDirectory;

    private String dataDirectory;

    public void afterPropertiesSet() {
        dataDirectory = (new File(rasterDataDirectory)).getAbsolutePath();
    }

	public String getTable(String longitude, String latitude, String startYear, String endYear) throws Exception {

        StringBuffer buffer = new StringBuffer();

        buffer.append("Year, GDD_may_sept_demosaic.tif, PPT_annual_demosaic.tif, PPT_may_sept_demosaic.tif, PPT_water_year.tif\n");

        Map<String,String[]> values = new HashMap<String,String[]>();
        for (String fileName : files) {
            String commandLine = String.format(
                "gdallocationinfo -valonly -wgs84 %s %s %s", dataDirectory + "/" + fileName, longitude, latitude);
            StreamSink streams[] = ProcessRunner.run(commandLine, "", new String[0], null);
            values.put(fileName, streams[0].toString().split("\\s+"));
        }

        String[] GDD_may_sept_demosaic  = values.get("GDD_may_sept_demosaic.tif");
        String[] PPT_annual_demosaic    = values.get("PPT_annual_demosaic.tif");
        String[] PPT_may_sept_demosaic  = values.get("PPT_may_sept_demosaic.tif");
        String[] PPT_water_year         = values.get("PPT_water_year.tif");

        int begin =  Integer.parseInt(startYear);
        if (begin < 1) begin = 1;
        int end =  Integer.parseInt(endYear);
        if (end > 1999) end = 1999;
        
        for (int year = begin; year <= end; ++year) {
            buffer.append(String.format("%d, %s, %s, %s, %s\n", 
                year,
                GDD_may_sept_demosaic[year],
                PPT_annual_demosaic[year],
                PPT_may_sept_demosaic[year],
                PPT_water_year[year]
            ));
        }

        return buffer.toString();
	}
}