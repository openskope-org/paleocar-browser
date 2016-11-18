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
@RequestMapping("${paleocar-browser-config.base}")
public class WebAppConfigController implements InitializingBean {

	private Object configData;

	@Value("${paleocar-browser-config.data}")	public String paleocarBrowserConfigData;
	@Value("${paleocar-browser-config.base}")  	public String paleocarBrowserConfigBase;
	@Value("${rastertile-service.base}")  		public String rasterTileServiceBase;
	@Value("${rasterdata-service.base}")  		public String rasterDataServiceBase;

	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		InputStream paleocarBrowserConfigDataStream = 
			WebAppConfigController.class.getClassLoader().getResourceAsStream(paleocarBrowserConfigData);
		configData = new Yaml().load(paleocarBrowserConfigDataStream);
    }

	@RequestMapping(value="config", method=RequestMethod.GET)
	public WebAppConfigResponse getConfiguration() {
		return new WebAppConfigResponse(
			paleocarBrowserConfigBase,
			rasterTileServiceBase,
            rasterDataServiceBase,
            configData
		);
	}
}