package app.tool.config;

public class DownloadConnection {
	
	String urlDownloadServer = "http://192.168.1.3/";
	//String urlDownloadServer = "http://192.168.43.210/";
	
	public DownloadConnection(String urlDownload) {
		urlDownloadServer = urlDownloadServer + urlDownload;
	}
	
	public String getUrlDownloadServer() {
		return urlDownloadServer;
	}
}
