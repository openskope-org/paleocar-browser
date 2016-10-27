package org.skope.service.locationinfo.controller;

import java.io.BufferedReader;
import java.io.StringReader;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.yesworkflow.Language;

import org.yesworkflow.config.YWConfiguration;

import org.yesworkflow.db.YesWorkflowDB;

import org.yesworkflow.extract.Extractor;
import org.yesworkflow.extract.DefaultExtractor;

import org.yesworkflow.model.DefaultModeler;
import org.yesworkflow.model.Modeler;
import org.yesworkflow.model.Workflow;
import org.yesworkflow.graph.Grapher;
import org.yesworkflow.graph.DotGrapher;

import org.yesworkflow.service.graph.model.GraphRequest;
import org.yesworkflow.service.graph.model.GraphResponse;
import org.yesworkflow.util.ProcessRunner;
import org.yesworkflow.util.StreamSink;

@RestController
@RequestMapping("/")
@EnableAutoConfiguration
@CrossOrigin
public class YWGraphServiceController {
    
    private final static String files[4] {
        "GDD_may_sept_demosaic.tif",
        "PPT_annual_demosaic.tif",
        "PPT_may_sept_demosaic.tif",
        "PPT_water_year.tif"   
    };

	@RequestMapping(value="detail", method=RequestMethod.GET)
    @ResponseBody
	public GraphResponse getDetail(
            @RequestParam(value="indexName", required=true),
            @RequestParam(value="x1", required=true),
            @RequestParam(value="y1", required=true),
            @RequestParam(value="x2", required=true),
            @RequestParam(value="y2", required=true),
            @RequestParam(value="zoom", required=true)
        ) throws Exception {

        Map<String,String[]> responseData;
        for (String fileName : files) {
            File file = new File(fileName);
            String values;
            values = null;
            try {			
                StreamSink streams[] = runGdalLocationInfo(dot);
                String valueString = streams[0].toString();
                values = valueString.split("\\s");
                System.out.println(values);
                responseData.put(fileName, values);
            } catch(Exception e) {
                error = e.getMessage();
            }
        }

		return new LocationInfoResponse(responseData);
	}

	// @RequestMapping(value="graph/cache/{id}", method=RequestMethod.GET)
	// public String getCachedGraph(@PathVariable Long id) {
	// 	return "{ \"cached_graphviz_file\": " + id + "}";
	// }
	
	
	private StreamSink[] runGdalLocationInfo(File file, double lat, double lon) throws Exception {
        String commandLine = String.format("gdallocationinfo -valonly -wgs84 \"%s\" %s %s", file.getAbsolutePath(), lat.toString(), lon.toString());
 		return ProcessRunner.run(commandLine + " -Tsvg", "", new String[0], null);
	}
}
