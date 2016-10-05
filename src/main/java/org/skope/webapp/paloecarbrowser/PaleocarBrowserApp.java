package org.skope.webapp.paleocarbrowser;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ComponentScan(basePackages="org.skope.webapp.paloecarbrowser")
public class PaleocarBrowserApp {

    public static void main(String[] args) {
        SpringApplication.run(PaleocarBrowserApp.class, args);
    }
}
