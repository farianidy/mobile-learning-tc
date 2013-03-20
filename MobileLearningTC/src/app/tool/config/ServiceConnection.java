package app.tool.config;

public class ServiceConnection {
	
	String urlServiceServer = "http://192.168.1.3/mobile-learning-tc";
	//String urlServiceServer = "http://192.168.43.210/mobile-learning-tc";
	
	public ServiceConnection(String urlService) {
		urlServiceServer = urlServiceServer + urlService;
	}
	
	public String getUrlServiceServer() {
		return urlServiceServer;
	}
}
