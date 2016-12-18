package org.openskope;

import org.yesworkflow.util.io.StdoutRecorder;
import org.yesworkflow.util.testing.Tests;

import static org.junit.Assert.*;

import org.junit.Test;

public class PaleocarBrowserAppTests extends Tests {

	private String expectedVersionBanner = String.format(
    		"-----------------------------------------------------------------------------"	+ EOL +
    		"PaleoCAR Browser %s-%s (branch master, commit %s)"								+ EOL +
    		"-----------------------------------------------------------------------------"	+ EOL,
    		PaleocarBrowserApp.versionInfo.buildVersion,
    		PaleocarBrowserApp.versionInfo.gitCommitsSinceTag,
    		PaleocarBrowserApp.versionInfo.gitCommitAbbrev
	);

	private String expectedHelpOutput = 
        	"Option                               Description                                                   "	+ EOL +
        	"------                               -----------                                                   "	+ EOL +
        	"-h, --help                           Displays this help.                                           "	+ EOL +
        	"--paleocar-browser-config.data-file  Sets path to file containing web application configuration.   "	+ EOL +
        	"--paleocar-browser-config.url        Sets URL of service providing web application configuration.  "	+ EOL +
        	"--raster-data-service.data-dir       Sets path to directory containing queryable raster data files."	+ EOL +
        	"--raster-data-service.url            Sets URL of raster data query service.                        "	+ EOL +
        	"--server.port                        Sets PaloeCAR Browser web application service port.           "	+ EOL +
        	"--static-tile-service.tiles-dir      Sets path to directory containing static map tile directory   "	+ EOL +
        	"                                       trees.                                                      "	+ EOL +
        	"--static-tile-service.url            Sets URL of service providing static map tiles for display.   "	+ EOL +
        	"-v, --version                        Shows version, git, and build details.                        "	+ EOL;

    @Test
    public void testStartServiceForArgs_HelpOption() throws Exception {

		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {
                PaleocarBrowserApp.startServiceForArgs(new String[] {"-h"});
			}
		});
		
		assertEquals(recorder.getStderrRecording(), expectedVersionBanner + expectedHelpOutput);
    }
}