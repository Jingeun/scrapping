package excel;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelInput {
	private final String fileName = "input.xlsx";
	private String category;
	private String searchWord;
	private int startPage, endPage;
	private String header;
	private int dollar;
	private String phone;
	private int takeback, exchange;
	
	public ExcelInput(){
		try {
			FileInputStream fis = new FileInputStream(fileName);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow row = sheet.getRow(1);
			this.category = row.getCell(0).getStringCellValue();
			this.searchWord = row.getCell(1).getStringCellValue();
			this.startPage = (int) Double.parseDouble(row.getCell(2).toString());
			this.endPage = (int) Double.parseDouble(row.getCell(3).toString());
			this.header = row.getCell(4).getStringCellValue();
			this.dollar = (int) Double.parseDouble(row.getCell(5).toString());
			this.phone = row.getCell(6).getStringCellValue();
			this.takeback = (int) Double.parseDouble(row.getCell(7).toString());
			this.exchange = (int) Double.parseDouble(row.getCell(8).toString());
			workbook.close();
			fis.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public String getPhone(){
		return this.phone;
	}
	public String getCategory(){
		return this.category;
	}
	public String getSearchWord(){
		return this.searchWord;
	}
	public int getStartPage(){
		return this.startPage;
	}
	public int getEndPage(){
		return this.endPage;
	}
	public String getHeader(){
		return this.header;
	}
	public int getDollar(){
		return this.dollar;
	}
	public int getTakeback(){
		return this.takeback;
	}
	public int getExchange(){
		return this.exchange;
	}
}
