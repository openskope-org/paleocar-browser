package org.openskope.app.paleocarbrowser;

import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@EnableAutoConfiguration 
@RequestMapping("/${paleocar-browser.prefix}/api/${paleocar-browser.version}/")
public class AppController implements InitializingBean {

    private String paleocarBrowserBaseUrl;
	private String rasterTileServiceBaseUrl;
	private String rasterDataServiceBaseUrl;
	private Object dataSets;
	private Yaml yamlParser = new Yaml();

	@Value("${paleocar-browser.host}") 	    public String paleocarBrowserHost;
	@Value("${paleocar-browser.prefix}")	public String paleocarBrowserPrefix;
	@Value("${paleocar-browser.version}")   public String paleocarBrowserVersion;
	@Value("${paleocar-browser.base}")  	public String paleocarBrowserBase;
	@Value("${paleocar-browser.datasets}")	public String paleocarBrowserDatasets;

	@Value("${rastertile-service.base}")  	public String rasterTileServiceBase;

	@Value("${rasterdata-service.host}")	public String rasterDataServiceHost;
	@Value("${rasterdata-service.prefix}")	public String rasterDataServicePrefix;
	@Value("${rasterdata-service.version}") public String rasterDataServiceVersion;
	@Value("${rasterdata-service.base}")  	public String rasterDataServiceBase;


	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		paleocarBrowserBaseUrl = restUrl(paleocarBrowserHost, paleocarBrowserPrefix, 
                                         paleocarBrowserVersion, paleocarBrowserBase);
		rasterTileServiceBaseUrl = restUrl(null, null, 
                                           null, rasterTileServiceBase);
		rasterDataServiceBaseUrl = restUrl(rasterDataServiceHost, rasterDataServicePrefix,
                                           rasterDataServiceVersion, rasterDataServiceBase);
		
		InputStream s = AppController.class.getClassLoader().getResourceAsStream(paleocarBrowserDatasets);
		dataSets = yamlParser.load(s);  
    }
	

	@RequestMapping(value="config", method=RequestMethod.GET)
	public AppConfigResponse getConfiguration() {
		return new AppConfigResponse(
			paleocarBrowserBaseUrl,
			rasterTileServiceBaseUrl,
            rasterDataServiceBaseUrl,
            dataSets
		);
	}

	private String restUrl(String host, String prefix, String version, String base) {

		if (base.length() > 0) {
			return base;
		} else if (host.trim().length()==0 || host.toUpperCase().equals("SHARED")) {
			return String.format("/%s/api/%s/", prefix, version);
		} else {
			return String.format("http://%s/%s/api/%s/", host, prefix, version);
		}
	}
}