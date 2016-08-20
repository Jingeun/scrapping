package http;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class httpConn {
	private final String useragent = "Mozilla/5.0 (compatible, MSIE 11, Windows, MSIE 11.0; en-US; rv:11.0) like Gecko";
	public Document getDoc(String url, IP tmpIP){
		Document doc = null;
		try {
//			conn.proxy(tmpIP.getIP(), tmpIP.getPort());
			System.setProperty("http.proxyHost", tmpIP.getIP());
			System.setProperty("http.proxyPort", "+tmpIP.getPort()+");
			
			Connection conn = Jsoup.connect(url);
			conn.timeout(0);
			conn.userAgent(useragent);
			
			doc = conn.ignoreHttpErrors(true).get();
			
		} catch (IOException e) { 
			System.out.println("error - 货肺款 proxy甫 历厘秦林技夸. ");
			System.exit(1);
		}
		return doc;
	}
	
	
}
