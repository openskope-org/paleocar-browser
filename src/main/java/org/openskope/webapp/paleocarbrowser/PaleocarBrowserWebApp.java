package org.openskope.webapp.paleocarbrowser;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ComponentScan(basePackages="org.openskope.webapp.paleocarbrowser," +
                            "org.openskope.service.rasterdata,"     +
                            "org.openskope.service.rastertile"      )
public class PaleocarBrowserWebApp {

    public static void main(String[] args) {
        SpringApplication.run(PaleocarBrowserWebApp.class, args);
    }
}
