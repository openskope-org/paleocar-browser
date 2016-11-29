package org.openskope.webapp.paleocarbrowser;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@EnableAutoConfiguration 
@RequestMapping("${paleocar-browser-config.url}")
public class WebAppConfigController implements InitializingBean {

	private Object configData;

	@Value("${paleocar-browser-config.data-file}")	public String paleocarBrowserConfigDataFile;
	@Value("${paleocar-browser-config.url}")  		public String paleocarBrowserConfigUrl;
	@Value("${raster-tile-service.url}")  			public String rasterTileServiceUrl;
	@Value("${raster-data-service.url}")  			public String rasterDataServiceUrl;

	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		InputStream paleocarBrowserConfigDataStream = 
			WebAppConfigController.class.getClassLoader().getResourceAsStream(paleocarBrowserConfigDataFile);
		configData = new Yaml().load(paleocarBrowserConfigDataStream);
    }

	@RequestMapping(value="config", method=RequestMethod.GET)
	public WebAppConfigResponse getConfiguration() {
		return new WebAppConfigResponse(
			paleocarBrowserConfigUrl,
			rasterTileServiceUrl,
            rasterDataServiceUrl,
            configData
		);
	}
}