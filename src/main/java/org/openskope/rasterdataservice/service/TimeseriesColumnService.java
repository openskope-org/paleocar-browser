package org.openskope.rasterdataservice.service;

import org.openskope.rasterdataservice.model.Timeseries;

import org.yesworkflow.util.exec.ProcessRunner;
import org.yesworkflow.util.exec.StreamSink;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TimeseriesColumnService implements InitializingBean {

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

	public Timeseries getTimeseries(String longitude, String latitude) throws Exception {

        Timeseries timeseries = new Timeseries();

        for (String fileName : files) {
            String commandLine = String.format(
                "gdallocationinfo -valonly -wgs84 %s %s %s", dataDirectory + "/" + fileName, longitude, latitude);
            System.out.println(commandLine);
            StreamSink streams[] = ProcessRunner.run(commandLine, "", new String[0], null);
            timeseries.put(fileName, streams[0].toString().split("\\s+"));
        }

		return timeseries;
	}
}