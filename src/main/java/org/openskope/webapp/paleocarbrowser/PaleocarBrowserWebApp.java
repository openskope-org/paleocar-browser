package org.openskope.webapp.paleocarbrowser;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.yesworkflow.util.cli.VersionInfo;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.PrintStream;
import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages="org.openskope.component," 				+
							"org.openskope.webapp.paleocarbrowser," +
                            "org.openskope.service.rasterdata,"     +
                            "org.openskope.service.rastertile"      )
public class PaleocarBrowserWebApp {

    public static VersionInfo versionInfo;

    private OptionSet options = null;
    private static PrintStream errStream;
    private static PrintStream outStream;    

    public static void main(String[] args) {

        ExitCode exitCode;

        versionInfo = VersionInfo.loadVersionInfoFromResource(
                "PaleoCAR Browser", 
                "https://github.com/openskope/paleocar-browser.git",
                "git.properties",
                "maven.properties");
        
        try {
            exitCode = startServiceForArgs(args);
        } catch (Exception e) {
            e.printStackTrace();
            exitCode = ExitCode.UNCAUGHT_ERROR;
        }

//        System.exit(exitCode.value());
    }

    public static ExitCode startServiceForArgs(String [] args) throws Exception {
        return startServiceForArgs(args, System.out, System.err);
    }
    
    public static ExitCode startServiceForArgs(String [] args, 
        PrintStream outStream, PrintStream errStream) throws Exception{

        PaleocarBrowserWebApp.outStream = outStream;
        PaleocarBrowserWebApp.errStream = errStream;

        try {

            OptionParser parser = createOptionsParser();
            OptionSet options;
            
            // parse the command line arguments and options
            try {
                options = parser.parse(args);
            } catch (OptionException exception) {
                throw new Exception(exception.getMessage());
            }

            // print detailed software version info and exit if requested
            if (options.has("v")) {
                errStream.print(versionInfo.versionBanner());
                errStream.print(versionInfo.versionDetails());
                return ExitCode.SUCCESS;
            }

            // print help and exit if requested
            if (options.has("h")) {
                errStream.print(versionInfo.versionBanner());
                // errStream.println(YW_CLI_USAGE_HELP);
                // errStream.println(YW_CLI_COMMAND_HELP);
                // parser.printHelpOn(errStream);
                // errStream.println();
                // errStream.println(YW_CLI_CONFIG_HELP);
                // errStream.println(YW_CLI_EXAMPLES_HELP);
                return ExitCode.SUCCESS;
            }

        SpringApplication.run(PaleocarBrowserWebApp.class, args);
        
        } catch (CliUsageException e) {
//            printToolUsageErrors(e.getMessage());
            return ExitCode.CLI_USAGE_ERROR;
        }

        return ExitCode.SUCCESS;
    }

    private static OptionParser createOptionsParser() throws Exception {
        
        OptionParser parser = null;

        parser = new OptionParser() {{
            acceptsAll(Arrays.asList("v", "version"), "Shows version, git, and build details.");
            acceptsAll(Arrays.asList("h", "help"), "Displays this help.");
        }};

        return parser;
    }


    public enum ExitCode {
        
        SUCCESS         ( 0),
        UNCAUGHT_ERROR  (-1),
        CLI_USAGE_ERROR (-2);
        
        private int value;
        private ExitCode(int value) { this.value = value; }
        public int value() { return value;}
    }

    public class CliUsageException extends Exception {};
}
