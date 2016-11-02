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
public class PaleocarBrowserController implements InitializingBean {

    private String paleocarBrowserBaseUrl;
	private String rasterTileServiceBaseUrl;
	private String rasterDataServiceBaseUrl;

	@Value("${paleocar-browser.host}") 	    public String paleocarBrowserHost;
	@Value("${paleocar-browser.prefix}")	public String paleocarBrowserPrefix;
	@Value("${paleocar-browser.version}")   public String paleocarBrowserVersion;

	@Value("${rastertile-service.host}")	public String rasterTileServiceHost;
	@Value("${rastertile-service.prefix}")	public String rasterTileServicePrefix;
	@Value("${rastertile-service.version}") public String rasterTileServiceVersion;

	@Value("${rasterdata-service.host}")	public String rasterDataServiceHost;
	@Value("${rasterdata-service.prefix}")	public String rasterDataServicePrefix;
	@Value("${rasterdata-service.version}") public String rasterDataServiceVersion;

	public void afterPropertiesSet() throws Exception {
		paleocarBrowserBaseUrl = restUrl(paleocarBrowserHost, paleocarBrowserPrefix, 
                                         paleocarBrowserVersion);
		rasterTileServiceBaseUrl = restUrl(rasterTileServiceHost, rasterTileServicePrefix, 
                                           rasterTileServiceVersion);
		rasterDataServiceBaseUrl = restUrl(rasterDataServiceHost, rasterDataServicePrefix,
                                           rasterDataServiceVersion);
    }
	
	@RequestMapping(value="config", method=RequestMethod.GET)
	public PaleocarBrowserConfigResponse getConfiguration() {
		return new PaleocarBrowserConfigResponse(
			paleocarBrowserBaseUrl,
			rasterTileServiceBaseUrl,
            rasterDataServiceBaseUrl
		);
	}

	private String restUrl(String host, String prefix, String version) {
		if (host.trim().length()==0 || host.toUpperCase().equals("SHARED")) {
			return String.format("/%s/api/%s/", prefix, version);
		} else {
			return String.format("http://%s/%s/api/%s/", host, prefix, version);
		}
	}
}