package app.util.connection;

public class DownloadConnection {
	
	//String urlDownloadServer = "http://192.168.1.5/";
	String urlDownloadServer = "http://192.168.173.1/";
	
	public DownloadConnection(String urlDownload) {
		urlDownloadServer = urlDownloadServer + urlDownload;
	}
	
	public String getUrlDownloadServer() {
		return urlDownloadServer;
	}
}
