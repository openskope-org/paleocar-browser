package org.openskope.service.rastertile;

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
            registry.addResourceHandler(rasterTileServiceBase + "/**")
                    .addResourceLocations("file:/" + rasterTilesDir + "/");
        }
    }
}