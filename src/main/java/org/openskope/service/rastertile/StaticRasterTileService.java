package org.openskope.service.rastertile;

import java.io.File;

import org.yesworkflow.util.uri.UriBase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticRasterTileService extends WebMvcConfigurerAdapter {

    @Value("${rastertile-service.tiles-dir}")
    private String rasterTilesDir;

    @Value("${rastertile-service.base}")
    private String rasterTileServiceBase;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	if (rasterTilesDir != null) {

            System.out.println("***** rasterTilesDir: " + rasterTilesDir + " *****");
            String resourceLocation = (new File(rasterTilesDir)).toURI().toString();
            System.out.println("***** Tiles resource location: " + resourceLocation + " *****");

            registry.addResourceHandler(rasterTileServiceBase + "/**")
                    .addResourceLocations(resourceLocation);
        }
    }
}