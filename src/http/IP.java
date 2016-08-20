package http;

public class IP {
	private String ip;
	private int port;
	
	public IP(String[] ip){
		this.ip = ip[0];
		this.port = Integer.parseInt(ip[1]);
	}
	
	public String getIP(){
		return this.ip;
	}
	
	public int getPort(){
		return this.port;
	}
}
