package org.openskope.webapp.paleocarbrowser;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@EnableAutoConfiguration 
@RequestMapping("/${paleocar-browser.prefix}/api/${paleocar-browser.version}/")
public class PaleocarBrowserController implements InitializingBean {

    private String paleocarBrowserBaseUrl;
	private String rasterTileServiceBaseUrl;
	private String rasterDataServiceBaseUrl;

	@Value("${paleocar-browser.host}") 	    public String paleocarBrowserHost;
	@Value("${paleocar-browser.prefix}")	public String paleocarBrowserPrefix;
	@Value("${paleocar-browser.version}")   public String paleocarBrowserVersion;
	@Value("${paleocar-browser.base}")  	public String paleocarBrowserBase;

	@Value("${rastertile-service.host}")	public String rasterTileServiceHost;
	@Value("${rastertile-service.prefix}")	public String rasterTileServicePrefix;
	@Value("${rastertile-service.version}") public String rasterTileServiceVersion;
	@Value("${rastertile-service.base}")  	public String rasterTileServiceBase;

	@Value("${rasterdata-service.host}")	public String rasterDataServiceHost;
	@Value("${rasterdata-service.prefix}")	public String rasterDataServicePrefix;
	@Value("${rasterdata-service.version}") public String rasterDataServiceVersion;
	@Value("${rasterdata-service.base}")  	public String rasterDataServiceBase;

	public void afterPropertiesSet() throws Exception {
		paleocarBrowserBaseUrl = restUrl(paleocarBrowserHost, paleocarBrowserPrefix, 
                                         paleocarBrowserVersion, paleocarBrowserBase);
		rasterTileServiceBaseUrl = restUrl(rasterTileServiceHost, rasterTileServicePrefix, 
                                           rasterTileServiceVersion, rasterTileServiceBase);
		rasterDataServiceBaseUrl = restUrl(rasterDataServiceHost, rasterDataServicePrefix,
                                           rasterDataServiceVersion, rasterDataServiceBase);
    }
	
	@RequestMapping(value="config", method=RequestMethod.GET)
	public PaleocarBrowserConfigResponse getConfiguration() {
		return new PaleocarBrowserConfigResponse(
			paleocarBrowserBaseUrl,
			rasterTileServiceBaseUrl,
            rasterDataServiceBaseUrl
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