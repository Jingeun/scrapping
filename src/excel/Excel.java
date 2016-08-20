package excel;

import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import http.IP;
import http.httpConn;

public class Excel {
	private final String getTitle = ".product-name";
	private final String getDiscountPrice = ".product-price-main #j-sku-discount-price";
	private final String getPrice = ".product-price-main #j-sku-price";
	private LinkedList<String> choiceList;
	private HashMap<String, LinkedList<String>> choiceListHashmap;
	private String productID;
	private String title;
	private String img;
	private double price;
	private String detail;
	public Excel(Element e, String productID, IP tmpIP){
		this.productID = productID;
		Elements detail_wrap = e.select(".detail-wrap");
		this.title = getTitleSource(detail_wrap);
		this.price = getPriceSource(detail_wrap);
		this.img = getImageSource(e.select(".detail-gallery"));
		getOptionSource(detail_wrap);
		this.detail = getDetailSource(tmpIP);
	}

	private String getDetailSource(IP tmpIP) {
		final String detailURL = "http://ko.aliexpress.com/getSubsiteDescModuleAjax.htm?productId=";
		httpConn conn = new httpConn();
		String detail = conn.getDoc(detailURL+productID, tmpIP).select("body").html();
		return detail.substring(28, detail.length()-2);
	}

	private void getOptionSource(Elements detail_wrap) {
		Elements choice = detail_wrap.select("#j-product-info-sku");
		if(!choice.text().trim().equals("")){
			choiceList = new LinkedList<String>();
			choiceListHashmap = new HashMap<String, LinkedList<String>>();
			Elements detailChoice = choice.select(".p-property-item");
			for(Element d:detailChoice){
				//선택 타이틀
				String detailTitle = d.getElementsByClass("p-item-title").text().trim().replace(':', ' ');
				/* Ships From 예외처리 */
				if(detailTitle.equals("Ships From")) continue;
				//선택 사항
				LinkedList<String> tmpList = new LinkedList<String>();
				Elements disableCheck = d.select("li");
				for(Element disable:disableCheck){
					if(disable.attr("class").indexOf("disable")>=0)
						continue;
					Elements dd = disable.select("a");
					if(detailTitle.equals("Color "))
						tmpList.add(dd.first().attr("title").trim());
					else if(detailTitle.equals("Material "))
						tmpList.add(dd.first().text().trim());
					//선택사항 추가있다면 추가 할 부분
				}
				if(tmpList.size()>0){
					choiceList.add(detailTitle);
					choiceListHashmap.put(detailTitle, tmpList);
				}
			}
		}
	}
	
	private String getImageSource(Elements detail_wrap) {
		return detail_wrap.select("img").attr("src");
	}

	private double getPriceSource(Elements detail_wrap) {
		double price = 0;
		String tmpPrice = detail_wrap.select(getDiscountPrice).text();
		if(tmpPrice.equals("")) tmpPrice = detail_wrap.select(getPrice).text();
		if(tmpPrice.indexOf("-")>=0){
			String token[] = tmpPrice.split("-");
			price = Double.parseDouble(token[0].trim());
		}else
			price = Double.parseDouble(tmpPrice);
		return price;
	}

	private String getTitleSource(Elements detail_wrap) {
		return detail_wrap.select(getTitle).text();
	}

	public String getTitle(){
		return this.title;
	}
	public String getImage(){
		return this.img;
	}
	public double getPrice(){
		return this.price;
	}
	public LinkedList<String> getChoiceList(){
		return this.choiceList;
	}
	public HashMap<String, LinkedList<String>> getChoiceListHashMap(){
		return this.choiceListHashmap;
	}
	public String getDetail(){
		return this.detail;
	}
	public String getProductID(){
		return this.productID;
	}
}
