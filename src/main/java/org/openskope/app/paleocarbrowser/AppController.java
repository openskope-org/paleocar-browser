package org.openskope.app.paleocarbrowser;

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
public class AppController implements InitializingBean {

	private Object dataSets;

	@Value("${paleocar-browser-config.data}")	public String dataSetDeclarationFile;
	@Value("${paleocar-browser-config.base}")  	public String paleocarBrowserConfigBase;
	@Value("${rastertile-service.base}")  		public String rasterTileServiceBase;
	@Value("${rasterdata-service.base}")  		public String rasterDataServiceBase;

	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		InputStream dataSetDeclarationYamlStream = 
			AppController.class.getClassLoader().getResourceAsStream(dataSetDeclarationFile);
		dataSets = new Yaml().load(dataSetDeclarationYamlStream);  
    }

	@RequestMapping(value="config", method=RequestMethod.GET)
	public AppConfigResponse getConfiguration() {
		return new AppConfigResponse(
			paleocarBrowserConfigBase,
			rasterTileServiceBase,
            rasterDataServiceBase,
            dataSets
		);
	}
}