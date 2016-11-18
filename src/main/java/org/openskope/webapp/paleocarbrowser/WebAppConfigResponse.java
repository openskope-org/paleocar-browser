package org.openskope.webapp.paleocarbrowser;


import java.util.Map;

import org.openskope.util.Uri;

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
        this.paleocarBrowserBaseUrl = Uri.ensureTerminalSlash(paleocarBrowserBaseUrl);
	    this.rasterTileServiceBaseUrl = Uri.ensureTerminalSlash(rasterTileServiceBaseUrl);
	    this.rasterDataServiceBaseUrl = Uri.ensureTerminalSlash(rasterDataServiceBaseUrl);
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