package org.openskope.paleocarbrowser.controller;

import org.openskope.paleocarbrowser.model.PaleocarBrowserConfig;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaleocarBrowserConfigService implements InitializingBean {

	private Object configData;

	@Value("${paleocar-browser-config.data-file}")	public String paleocarBrowserConfigDataFile;
	@Value("${paleocar-browser-config.url}")  		public String paleocarBrowserConfigUrl;
	@Value("${raster-tile-service.url}")  			public String rasterTileServiceUrl;
	@Value("${raster-data-service.url}")  			public String rasterDataServiceUrl;

	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		InputStream paleocarBrowserConfigDataStream = 
			PaleocarBrowserConfigService.class.getClassLoader().getResourceAsStream(paleocarBrowserConfigDataFile);
		configData = new Yaml().load(paleocarBrowserConfigDataStream);
    }

	public PaleocarBrowserConfig getConfig() {
		return new PaleocarBrowserConfig(
			paleocarBrowserConfigUrl,
			rasterTileServiceUrl,
            rasterDataServiceUrl,
            configData
		);
	}
}