package org.openskope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.yesworkflow.util.cli.VersionInfo;

import joptsimple.BuiltinHelpFormatter;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.PrintStream;
import java.util.Arrays;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class PaleocarBrowserApp {

    protected static Class<PaleocarBrowserApp> springBootAppClass = PaleocarBrowserApp.class;

    public static VersionInfo versionInfo = VersionInfo.loadVersionInfoFromResource(
            "PaleoCAR Browser", 
            "https://github.com/openskope/paleocar-browser.git",
            "git.properties",
            "maven.properties");
        
    public static void main(String[] args) throws Exception {
    	startServiceForArgs(args, System.out, System.err);
    }

    public static void startServiceForArgs(String [] args, 
    		PrintStream outStream, PrintStream errStream) throws Exception{

    	System.setErr(errStream);
    	System.setOut(outStream);

        OptionParser parser = createOptionsParser();
        OptionSet options;
        
        // parse the command line arguments and options
        try {
            options = parser.parse(args);
        } catch (OptionException exception) {
            errStream.println(exception.getMessage());
            return;
        }

        if (options.has("v")) {
            // print detailed software version info and exit
            errStream.print(versionInfo.versionBanner());
            errStream.print(versionInfo.versionDetails());
            return;
        }

        if (options.has("h")) {
            // print help and exit if requested
            errStream.print(versionInfo.versionBanner());
            // errStream.println(CLI_USAGE_HELP);
            // errStream.println(CLI_COMMAND_HELP);
            parser.printHelpOn(errStream);
            // errStream.println();
            // errStream.println(CLI_CONFIG_HELP);
            // errStream.println(CLI_EXAMPLES_HELP);
            return;
        }
        
        try {
        	SpringApplication.run(springBootAppClass, args);
        } catch(Exception e) {
        	System.err.println("Error starting Spring Boot Application");
            e.printStackTrace();
        }
    }

    private static OptionParser createOptionsParser() throws Exception {
        
        OptionParser parser = new OptionParser() {{
            acceptsAll(Arrays.asList("v", "version"), "Shows version, git, and build details.");
            acceptsAll(Arrays.asList("h", "help"), "Displays this help.");
            acceptsAll(Arrays.asList("server.port"), "Sets PaloeCAR Browser web application service port.");
            acceptsAll(Arrays.asList("paleocar-browser-config.url"), "Sets URL of service providing web application configuration.");
            acceptsAll(Arrays.asList("paleocar-browser-config.data-file"), "Sets path to file containing web application configuration.");
            acceptsAll(Arrays.asList("raster-data-service.url"), "Sets URL of raster data query service.");
            acceptsAll(Arrays.asList("raster-data-service.data-dir"), "Sets path to directory containing queryable raster data files.");
            acceptsAll(Arrays.asList("static-tile-service.url"), "Sets URL of service providing static map tiles for display.");
            acceptsAll(Arrays.asList("static-tile-service.tiles-dir"),"Sets path to directory containing static map tile directory trees.");
        }};

        parser.formatHelpWith(new BuiltinHelpFormatter(128, 2));

        return parser;
    }
    
    PaleocarBrowserApp() throws Exception {
    	throw new Exception("The class PaleocarBrowserApp may not be instantiated");
    }
}
