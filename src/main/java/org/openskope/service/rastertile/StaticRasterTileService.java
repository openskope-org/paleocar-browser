package org.openskope.service.rastertile;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StaticRasterTileService extends WebMvcConfigurerAdapter {

    @Value("${rastertile-service.static-data-dir}")
    private String rasterTileStaticDataDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    	if (rasterTileStaticDataDir != null) {
            registry.addResourceHandler("/**").addResourceLocations("file:" + rasterTileStaticDataDir);
        }
    }
}