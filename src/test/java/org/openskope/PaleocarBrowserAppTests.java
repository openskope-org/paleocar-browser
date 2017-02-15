package org.openskope;

import org.yesworkflow.util.io.StdoutRecorder;
import org.yesworkflow.util.testing.Tests;

import static org.junit.Assert.*;

import org.junit.Test;

public class PaleocarBrowserAppTests extends Tests {

	private String expectedVersionBanner = String.format(
    		"-----------------------------------------------------------------------------"	+ EOL +
    		"PaleoCAR Browser %s-%s (branch %s, commit %s)"									+ EOL +
    		"-----------------------------------------------------------------------------"	+ EOL,
    		PaleocarBrowserApp.versionInfo.buildVersion,
    		PaleocarBrowserApp.versionInfo.gitCommitsSinceTag,
    		PaleocarBrowserApp.versionInfo.gitBranch,
    		PaleocarBrowserApp.versionInfo.gitCommitAbbrev
	);

	private String expectedVersionInfo = String.format(
			"Remote repo: %s"				+ EOL +
			"Git branch: %s"				+ EOL +
			"Last commit: %s"				+ EOL +
			"Commit time: %s"				+ EOL +
			"Most recent tag: %s"			+ EOL +
			"Commits since tag: %s"			+ EOL +
			"Builder name: %s"				+ EOL +
			"Builder email: %s"				+ EOL +
			"Build host: %s"				+ EOL +
			"Build platform: %s"			+ EOL +
			"Build Java VM: %s"				+ EOL +
			"Build Java version: JDK %s"	+ EOL +
			"Build time: %s"				+ EOL,
    		PaleocarBrowserApp.versionInfo.officialRepoUrl,
    		PaleocarBrowserApp.versionInfo.gitBranch,
    		PaleocarBrowserApp.versionInfo.gitCommitId,
    		PaleocarBrowserApp.versionInfo.gitCommitTime,
    		PaleocarBrowserApp.versionInfo.gitClosestTag,
    		PaleocarBrowserApp.versionInfo.gitCommitsSinceTag,
    		PaleocarBrowserApp.versionInfo.buildUserName,
    		PaleocarBrowserApp.versionInfo.buildUserEmail,
    		PaleocarBrowserApp.versionInfo.buildHost,
    		PaleocarBrowserApp.versionInfo.buildPlatform,
    		PaleocarBrowserApp.versionInfo.buildJavaVM,
    		PaleocarBrowserApp.versionInfo.buildJavaVersion,
    		PaleocarBrowserApp.versionInfo.buildTime
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

	
    @Test public void testMain_HelpOption() throws Exception {
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {
                PaleocarBrowserApp.main(new String[] {"-h"});
			}
		});
		assertEquals(expectedVersionBanner + expectedHelpOutput, recorder.getStderrRecording());
		assertEquals("", recorder.getStdoutRecording());
    }

    @Test public void testStartServiceForArgs_HelpOption() throws Exception {
        PaleocarBrowserApp.startServiceForArgs(new String[] {"-h"}, stdoutStream, stderrStream);
        assertEquals(expectedVersionBanner + expectedHelpOutput, stderrBuffer.toString());
        assertEquals("", stdoutBuffer.toString());
    }

    @Test public void testMain_VersionOption() throws Exception {
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {
                PaleocarBrowserApp.main(new String[] {"-v"});
			}
		});
		assertEquals(expectedVersionBanner + expectedVersionInfo, recorder.getStderrRecording());
		assertEquals("", recorder.getStdoutRecording());
    }
    
    @Test public void testStartServiceForArgs_VersionOption() throws Exception {
        PaleocarBrowserApp.startServiceForArgs(new String[] {"-v"}, stdoutStream, stderrStream);
        assertEquals(expectedVersionBanner + expectedVersionInfo, stderrBuffer.toString());
        assertEquals("", stdoutBuffer.toString());
    }
    
    @Test public void testStartServiceForArgs_BadOption() throws Exception {
    	PaleocarBrowserApp.startServiceForArgs(new String[] {"--BAD"}, stdoutStream, stderrStream);
        assertEquals("BAD is not a recognized option" + EOL, stderrBuffer.toString());
        assertEquals("", stdoutBuffer.toString());
    }
    
    @Test public void testMain_NullSpringApplication() throws Exception {
    	PaleocarBrowserApp.springBootAppClass = null;
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {
				PaleocarBrowserApp.main(new String[] {});
			}
		});
		assertTrue(recorder.getStderrRecording().startsWith("Error starting Spring Boot Application"));
    }
}