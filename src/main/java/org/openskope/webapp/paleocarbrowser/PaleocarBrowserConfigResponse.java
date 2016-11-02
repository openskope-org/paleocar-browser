package org.openskope.webapp.paleocarbrowser;

public class PaleocarBrowserConfigResponse {
    
    private String paleocarBrowserBaseUrl;
	private String rasterTileServiceBaseUrl;
	private String rasterDataServiceBaseUrl;

	public PaleocarBrowserConfigResponse() {}

    public PaleocarBrowserConfigResponse(
        String paleocarBrowserBaseUrl, 
        String rasterTileServiceBaseUrl,
	    String rasterDataServiceBaseUrl
    ) {
        this();
        this.paleocarBrowserBaseUrl = paleocarBrowserBaseUrl;
	    this.rasterTileServiceBaseUrl = rasterTileServiceBaseUrl;
	    this.rasterDataServiceBaseUrl = rasterDataServiceBaseUrl;
    }

    public void setPaleocarBrowserBaseUrl(String paleocarBrowserBaseUrl) {
        this.paleocarBrowserBaseUrl = paleocarBrowserBaseUrl;
    }

    public String getPaleocarBrowserBaseUrl() {
        return this.paleocarBrowserBaseUrl;
    }

    public void setRasterTileServiceBaseUrl(String rasterTileServiceBaseUrl) {
        this.rasterTileServiceBaseUrl = rasterTileServiceBaseUrl;
    }

    public String getRasterTileServiceBaseUrl() {
        return this.rasterTileServiceBaseUrl;
    }

    public void setRasterDataServiceBaseUrl(String rasterDataServiceBaseUrl) {
        this.rasterDataServiceBaseUrl = rasterDataServiceBaseUrl;
    }

    public String getRasterDataServiceBaseUrl() {
        return this.rasterDataServiceBaseUrl;
    }
}