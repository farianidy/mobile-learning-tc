package app.util.connection;

public class ServiceConnection {
	
	String urlServiceServer = "http://192.168.1.4/mobile-learning-tc";
//	String urlServiceServer = "http://192.168.173.1/mobile-learning-tc";
	
	public ServiceConnection(String urlService) {
		urlServiceServer = urlServiceServer + urlService;
	}
	
	public String getUrlServiceServer() {
		return urlServiceServer;
	}
}
