package app.tool.config;

public class ServiceConnection {
	
	String urlServiceServer = "http://10.0.2.2/mobile-learning-tc";
	
	public ServiceConnection(String urlService) {
		urlServiceServer = urlServiceServer + urlService;
	}
	
	public String getUrlServiceServer() {
		return urlServiceServer;
	}
}
