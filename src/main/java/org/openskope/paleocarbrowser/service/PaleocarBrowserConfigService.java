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

	@Value("${paleocar-browser-config.data-file}")	private String paleocarBrowserConfigDataFile;
	@Value("${paleocar-browser-config.url}")  		private String paleocarBrowserConfigUrl;
	@Value("${raster-data-service.url}")  			private String rasterDataServiceUrl;
	@Value("${static-tile-service.url}")  			private String staticTileServiceUrl;

	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		InputStream paleocarBrowserConfigDataStream = 
			PaleocarBrowserConfigService.class.getClassLoader().getResourceAsStream(paleocarBrowserConfigDataFile);
		configData = new Yaml().load(paleocarBrowserConfigDataStream);
    }

	public PaleocarBrowserConfig getConfig() {
		return new PaleocarBrowserConfig(
			paleocarBrowserConfigUrl,
			staticTileServiceUrl,
            rasterDataServiceUrl,
            configData
		);
	}
}