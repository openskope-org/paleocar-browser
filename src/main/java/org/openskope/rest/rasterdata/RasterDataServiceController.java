package org.openskope.rest.rasterdata;

import java.io.File;
import java.io.Writer;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;

import org.yesworkflow.util.exec.ProcessRunner;
import org.yesworkflow.util.exec.StreamSink;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.InitializingBean;


@RestController 
@EnableAutoConfiguration 
@CrossOrigin 
@RequestMapping("${raster-data-service.url}/")
public class RasterDataServiceController implements InitializingBean {
    
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

	@RequestMapping(value="/timeseries", method=RequestMethod.GET)
    @ResponseBody
	public TimeseriesResponse getTimeSeries(
            @RequestParam(value="long", required=true) String longitude,
            @RequestParam(value="lat", required=true) String latitude
        ) throws Exception {

        TimeseriesResponse response = new TimeseriesResponse();

        for (String fileName : files) {
            String commandLine = String.format(
                "gdallocationinfo -valonly -wgs84 %s %s %s", dataDirectory + "/" + fileName, longitude, latitude);
            System.out.println(commandLine);
            StreamSink streams[] = ProcessRunner.run(commandLine, "", new String[0], null);
            response.put(fileName, streams[0].toString().split("\\s+"));
        }

		return response;
	}

	@RequestMapping(value="/timeseries-download", method=RequestMethod.GET)
	public void getTimeSeriesDownload(
            HttpServletResponse response,
            @RequestParam(value="long", required=true) String longitude,
            @RequestParam(value="lat", required=true) String latitude,
            @RequestParam(value="startYear", required=true) String startYear,
            @RequestParam(value="endYear", required=true) String endYear
        ) throws Exception {

        String csvFileName = "SKOPE.csv";
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", 
                            String.format("attachment; filename=\"%s\"", csvFileName));

        //TODO: Try using @ResponseBody on method and return string containing CSV content
        Writer writer = response.getWriter();
        writer.write("Year, GDD_may_sept_demosaic.tif, PPT_annual_demosaic.tif, PPT_may_sept_demosaic.tif, PPT_water_year.tif\n");

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
            writer.write(String.format("%d, %s, %s, %s, %s\n", 
                year,
                GDD_may_sept_demosaic[year],
                PPT_annual_demosaic[year],
                PPT_may_sept_demosaic[year],
                PPT_water_year[year]
            ));
        }


	}
}
