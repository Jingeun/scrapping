package ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import excel.Excel;
import excel.ExcelInput;
import excel.ExcelOutputNaver;
import http.IP;
import http.Proxy;
import http.httpConn;

public class Main {
	private final String url;
	private LinkedList<IP> proxy;
	private LinkedList<String> urlList;
	private LinkedList<Excel> excel;
	private static int count;
	private static int productCount;
	
	public Main(String filePath){
		this.url = "http://www.aliexpress.com/wholesale?SearchText=";
		Proxy proxy = new Proxy(filePath);
		this.proxy = proxy.getProxy();
		this.excel = new LinkedList<Excel>();
		this.productCount = 1;
	}
	
	private void getMainPageUrl(String search, int page) {
		this.urlList = new LinkedList<String>();
		Random random = new Random();
		IP tmpIP = proxy.get(random.nextInt(proxy.size()));
		String url = this.url+search+"&page="+page;

		httpConn conn = new httpConn();
		Document firstDoc = conn.getDoc(url, tmpIP);
		if(firstDoc==null){
			System.out.println("error - Document is null");
			System.exit(1);
		}
		Elements firstElements = firstDoc.select("#hs-list-items .list-item .detail .history-item");
		for(Element e: firstElements)
			urlList.add("http:"+e.attr("href"));
		
		Document secondDoc = Jsoup.parse(firstDoc.select("#lazy-render").html());
		Elements secondElements = secondDoc.select(".list-item .detail .history-item");
		for(Element e: secondElements)
			urlList.add("http:"+e.attr("href"));
		
//		for(int i=0;i<urlList.size();i++)
//			System.out.println(i+" : "+urlList.get(i));
	}

	
	private void getDetailPage(int currentPage, int endPage, Date date) {
		Random random = new Random();
		int size = urlList.size();
		for(int i=0;i<size;i++){
			String detailURL = urlList.get(i);
			IP tmpIP = proxy.get(random.nextInt(proxy.size()));
			httpConn conn = new httpConn();
			Document doc = conn.getDoc(detailURL, tmpIP);
//			Document doc = conn.getDoc("http://www.aliexpress.com/item/Phone-case-for-Apple-iphone-6-case-4-7-iphone6-6S-Cases-Vintage-Flower-Pattern-Fashion/32555253895.html?ws_ab_test=searchweb201556_8,searchweb201602_5_10017_406_9013,searchweb201603_2&btsid=f68ce0e8-e74b-4ce4-88e7-60863cf1bbe0", tmpIP);
			
			int startIndex = 0, endIndex = detailURL.indexOf(".html?");
			for(int j=endIndex;j>=0;j--)
				if(detailURL.charAt(j)=='/'){
					startIndex = j+1;
					break;
				}
			final String productID = detailURL.substring(startIndex, endIndex);
			
			System.out.println(" "+currentPage+" 페이지      "+(productCount++)+"/"+size+"   proxy IP: "+tmpIP.getIP()+"   Product ID: "+productID);
			System.out.println("주소 :"+detailURL);
			Elements ele = doc.select(".detail");
			for(Element e:ele)
				excel.add(new Excel(e, productID, tmpIP));
			System.out.println("--------------------------------------");
		}
	}

	public static void main(String[] args) {
		Main main = new Main("proxy.txt");
		ExcelInput input = new ExcelInput();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		Date date = new Date();
		final String folderName = "네이버_"+format.format(date);
		File f= new File(folderName);
		if(!f.mkdirs()){
			System.out.println("폴더생성실패");
			System.exit(1);
		}
		for(int i=input.getStartPage();i<=input.getEndPage();i++){
			main.getMainPageUrl(TranslateBlank(input.getSearchWord()), i);
			main.getDetailPage(i, input.getEndPage(), date);
		}
		main.makeExcel(input, date, folderName);
	}

	private void makeExcel(ExcelInput input, Date date, String folderName) {
		ExcelOutputNaver output = new ExcelOutputNaver(input, excel, date);
		output.makeExcelOutputNaver(folderName);
	}
	
	private static String TranslateBlank(String search) {
		return search.replaceAll(" ", "+");
	}

}
