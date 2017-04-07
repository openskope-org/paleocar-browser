package org.openskope.paleocarbrowser.service;

import org.openskope.paleocarbrowser.model.PaleocarBrowserConfig;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaleocarBrowserConfigService implements InitializingBean {

	private Object configData;

	@Value("${paleocar-browser.config-file}")	    private String paleocarBrowserConfigDataFile;
	@Value("${paleocar-browser.config-endpoint}")  	private String paleocarBrowserConfigEndpoint;
	@Value("${raster-data-service.endpoint}")  		private String rasterDataServiceEndpoint;
	@Value("${static-tile-service.endpoint}")  		private String staticTileServiceEndpoint;

	public void afterPropertiesSet() throws Exception {
		 InputStream paleocarBrowserConfigDataStream = 
		 	PaleocarBrowserConfigService.class.getClassLoader().getResourceAsStream(paleocarBrowserConfigDataFile);
		 configData = new Yaml().load(paleocarBrowserConfigDataStream);
    }

	public PaleocarBrowserConfig getConfig() {
		return new PaleocarBrowserConfig(
			paleocarBrowserConfigEndpoint,
			staticTileServiceEndpoint,
            rasterDataServiceEndpoint,
            configData
		);
	}
}