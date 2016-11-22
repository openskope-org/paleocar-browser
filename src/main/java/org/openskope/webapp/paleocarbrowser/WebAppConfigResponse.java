package org.openskope.webapp.paleocarbrowser;

import org.yesworkflow.util.uri.UriBase;

public class WebAppConfigResponse {
    
    private String paleocarBrowserBaseUrl;
	private String rasterTileServiceBaseUrl;
	private String rasterDataServiceBaseUrl;
	private Object dataSets;

    public WebAppConfigResponse(
        String paleocarBrowserBaseUrl, 
        String rasterTileServiceBaseUrl,
	    String rasterDataServiceBaseUrl,
	    Object dataSets
    ) {
        this.paleocarBrowserBaseUrl = UriBase.ensureTerminalSlash(paleocarBrowserBaseUrl);
	    this.rasterTileServiceBaseUrl = UriBase.ensureTerminalSlash(rasterTileServiceBaseUrl);
	    this.rasterDataServiceBaseUrl = UriBase.ensureTerminalSlash(rasterDataServiceBaseUrl);
	    this.dataSets = dataSets;
    }

    public String getPaleocarBrowserBaseUrl() {
        return this.paleocarBrowserBaseUrl;
    }

    public String getRasterTileServiceBaseUrl() {
        return this.rasterTileServiceBaseUrl;
    }

    public String getRasterDataServiceBaseUrl() {
        return this.rasterDataServiceBaseUrl;
    }
    
    public Object getDataSets() {
        return this.dataSets;
    }
}