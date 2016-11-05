package org.openskope.app.paleocarbrowser;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ComponentScan(basePackages="org.openskope.app.paleocarbrowser,org.openskope.service.rasterdata")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
