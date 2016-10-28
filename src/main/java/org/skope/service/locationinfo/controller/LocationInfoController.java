package org.skope.service.locationinfo.controller;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.File;

import org.skope.util.ProcessRunner;
import org.skope.util.StreamSink;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.skope.service.locationinfo.model.LocationInfoResponse;

@RestController @EnableAutoConfiguration @CrossOrigin @RequestMapping("/browse")
public class LocationInfoController {
    
    private final static String files[];
    
    static {
        files = new String[] {
            "GDD_may_sept_demosaic.tif",
            "PPT_annual_demosaic.tif",
            "PPT_may_sept_demosaic.tif",
            "PPT_water_year.tif"   
        };
    };

	@RequestMapping(value="detail", method=RequestMethod.GET)
    @ResponseBody
	public LocationInfoResponse getDetail(
            @RequestParam(value="x1", required=true) String longitude,
            @RequestParam(value="y1", required=true) String latitude
        ) throws Exception {

        LocationInfoResponse response = new LocationInfoResponse();

        for (String fileName : files) {
            String commandLine = String.format(
                "gdallocationinfo -valonly -wgs84 %s %s %s", "/home/tmcphill/skope/data/" + fileName, latitude, longitude);
            System.out.println(commandLine);
            StreamSink streams[] = ProcessRunner.run(commandLine, "", new String[0], null);
            response.put(fileName, streams[0].toString().split("\\s"));
        }

		return response;
	}
}
