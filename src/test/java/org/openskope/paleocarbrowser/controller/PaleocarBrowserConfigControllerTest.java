package org.openskope.paleocarbrowser.controller;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertSame;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

import org.openskope.paleocarbrowser.model.PaleocarBrowserConfig;
import org.openskope.paleocarbrowser.service.PaleocarBrowserConfigService;

public class PaleocarBrowserConfigControllerTest {

    @Mock private PaleocarBrowserConfigService configService;
    @InjectMocks private PaleocarBrowserConfigController configController;

    @Before public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test public void getConfigReturnsConfigProvidedByConfigService() {
        PaleocarBrowserConfig expectedConfig = new PaleocarBrowserConfig(null,null,null,null);
        when(configService.getConfig()).thenReturn(expectedConfig);
        assertSame(expectedConfig, configController.getConfig());
    }
}