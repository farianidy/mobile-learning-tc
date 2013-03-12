package app.tool.config;

public class DownloadConnection {
	
	String urlDownloadServer = "http://10.0.2.2/";
	
	public DownloadConnection(String urlDownload) {
		urlDownloadServer = urlDownloadServer + urlDownload;
	}
	
	public String getUrl() {
		return urlDownloadServer;
	}
}
