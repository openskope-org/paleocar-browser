package org.openskope.webapp.paleocarbrowser;

import org.yesworkflow.util.uri.UriBase;

public class WebAppConfigResponse {
    
    private String paleocarBrowserUrl;
	private String rasterTileServiceUrl;
	private String rasterDataServiceUrl;
	private Object dataSets;

    public WebAppConfigResponse(
        String paleocarBrowserUrl, 
        String rasterTileServiceUrl,
	    String rasterDataServiceUrl,
	    Object dataSets
    ) {
        this.paleocarBrowserUrl = UriBase.ensureTerminalSlash(paleocarBrowserUrl);
	    this.rasterTileServiceUrl = UriBase.ensureTerminalSlash(rasterTileServiceUrl);
	    this.rasterDataServiceUrl = UriBase.ensureTerminalSlash(rasterDataServiceUrl);
	    this.dataSets = dataSets;
    }

    public String getPaleocarBrowserUrl() {
        return this.paleocarBrowserUrl;
    }

    public String getRasterTileServiceUrl() {
        return this.rasterTileServiceUrl;
    }

    public String getRasterDataServiceUrl() {
        return this.rasterDataServiceUrl;
    }
    
    public Object getDataSets() {
        return this.dataSets;
    }
}