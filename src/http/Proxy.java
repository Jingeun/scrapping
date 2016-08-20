package http;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Proxy {
	private LinkedList<IP> list;
	
	public Proxy(String filePath){
		this.list = new LinkedList<IP>();
		makeProxy(filePath);
	}
	
	private void makeProxy(String filePath) {
		try {
			Scanner in = new Scanner(new File(filePath));
			while(in.hasNextLine()){
				String ip = in.nextLine();
				String token[] = ip.split(":");
				this.list.add(new IP(token));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public LinkedList<IP> getProxy(){
		return this.list;
	}
}
