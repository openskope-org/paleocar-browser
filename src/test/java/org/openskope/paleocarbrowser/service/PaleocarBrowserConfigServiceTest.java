package org.openskope.paleocarbrowser.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import org.openskope.paleocarbrowser.service.PaleocarBrowserConfigService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(classes = PaleocarBrowserConfigServiceTest.SpringConfig.class)
public class PaleocarBrowserConfigServiceTest {

    @Autowired private PaleocarBrowserConfigService configService;

    @Test public void testConfigGetDataSets() {
        assertEquals("{name=test}", configService.getConfig().getDataSets().toString()); 
    }

    @Test public void testConfigPaleocarBrowserUrl() {
        assertEquals("http://paleocar-browser/", configService.getConfig().getPaleocarBrowserUrl()); 
    }

    @Test public void testConfigGetRasterDataServiceUrl() {
        assertEquals("http://raster-data-service/", configService.getConfig().getRasterDataServiceUrl()); 
    }

    @Test public void testConfigGetRasterTileServiceUrl() {
        assertEquals("http://raster-tile-service/", configService.getConfig().getRasterTileServiceUrl()); 
    }

    @Configuration public static class SpringConfig {

        @Bean public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
            final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            Properties properties = new Properties();
            properties.setProperty("paleocar-browser-config.data-file", "datasets_test.yaml");
            properties.setProperty("paleocar-browser-config.url", "http://paleocar-browser/");
            properties.setProperty("raster-data-service.url", "http://raster-data-service");
            properties.setProperty("static-tile-service.url", "http://raster-tile-service");
            pspc.setProperties(properties);
            return pspc;
        }
        
        @Bean public PaleocarBrowserConfigService getPaleocarConfigService() {
            return new PaleocarBrowserConfigService();
        }
    }
}