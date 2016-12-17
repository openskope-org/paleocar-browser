package org.openskope.paleocarbrowser.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertSame;

public class PaleocarBrowserConfigTest {

    private PaleocarBrowserConfig config;

    @Before
    public void init() {
        config = new PaleocarBrowserConfig(
            "http://paleocar-browser/",
            "http://raster-tile-service/",
            "http://raster-data-service/",
            "datasets"
            );
    }

    @Test
    public void getPaleocarBrowserUrlTest() {
        assertSame("http://paleocar-browser/", config.getPaleocarBrowserUrl());
    }

    @Test
    public void getRasterTileServiceUrlTest() {
        assertSame("http://raster-tile-service/", config.getRasterTileServiceUrl());
    }

    @Test
    public void getRasterDataServiceUrlTest() {
        assertSame("http://raster-data-service/", config.getRasterDataServiceUrl());
    }

    @Test
    public void getDataSetsTest() {
        assertSame("datasets", config.getDataSets());
    }
}