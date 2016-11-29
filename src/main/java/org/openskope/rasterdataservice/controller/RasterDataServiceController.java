package org.openskope.rasterdataservice.controller;

import org.openskope.rasterdataservice.model.Timeseries;
import org.openskope.rasterdataservice.service.TimeseriesColumnService;
import org.openskope.rasterdataservice.service.TimeseriesTableService;

import java.io.File;
import java.io.Writer;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

@RestController 
@EnableAutoConfiguration
@CrossOrigin 
@RequestMapping("${raster-data-service.url}/")
public class RasterDataServiceController {
    
    @Autowired public TimeseriesColumnService timeseriesColumnService;
    @Autowired public TimeseriesTableService timeseriesTableService;

	@RequestMapping(value="/timeseries", method=RequestMethod.GET)
    public @ResponseBody Timeseries getTimeSeries(
            @RequestParam(value="long", required=true) String longitude,
            @RequestParam(value="lat", required=true) String latitude
        ) throws Exception {

        return timeseriesColumnService.getTimeseries(longitude, latitude);
	}

	@RequestMapping(value="/timeseries-download", method=RequestMethod.GET)
    public @ResponseBody String getTimeSeriesDownload(
            HttpServletResponse response,
            @RequestParam(value="long", required=true) String longitude,
            @RequestParam(value="lat", required=true) String latitude,
            @RequestParam(value="startYear", required=true) String startYear,
            @RequestParam(value="endYear", required=true) String endYear
        ) throws Exception {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=SKOPE.csv");
        return timeseriesTableService.getTable(longitude, latitude, startYear, endYear);
	}
}
