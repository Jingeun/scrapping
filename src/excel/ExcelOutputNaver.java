package excel;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

public class ExcelOutputNaver {
	private String fileName;
	private LinkedList<Excel> excel;
	private ExcelInput input;
	private String[] menu = {
			"상품상태", "카테고리ID", "카테고리 속성 코드", "상품명", "홍보문구",
			"판매가", "재고수량", "A/S 안내내용", "A/S 전화번호", "대표 이미지 파일명",
			"리스트뷰 이미지 파일명", "이미지뷰 이미지 파일명", "큰이미지뷰 이미지 파일명", "갤러리뷰 이미지 파일명", "추가 이미지 파일명",
			"상품 상세정보", "판매자 상품코드", "판매자 바코드", "제조사", "브랜드",
			"모델 코드", "제조일자", "유효일자", "부가세", "미성년자 구매",
			"구매평 노출여부", "스토어찜회원 전용여부", "원산지 코드", "수입사", "복수원산지 여부", 
			"원산지 직접입력", "배송방법", "배송비 유형", "기본배송비", "배송비 결제방식", 
			"조건부무료-상품판매가합계", "수량별부과-수량", "반품배송비", "교환배송비", "지역별 차등배송비 정보", 
			"별도설치비	", "판매자 특이사항", "즉시할인 값", "즉시할인 단위", "복수구매할인 조건 값",
			"복수구매할인 조건 단위", "복수구매할인 값", "복수구매할인 단위", "상품구매시 포인트 지급 값", "상품구매시 포인트 지급 단위",
			"구매평 작성시 지급 포인트", "프리미엄 구매평 작성시 지급 포인트", "스토어찜회원 추가 지급 포인트", "무이자 할부 개월", "사은품",
			"옵션형태", "옵션명", "옵션값", "옵션가", "옵션 재고수량",
			"추가상품명", "추가상품값", "추가상품가", "추가상품 재고수량", "상품정보제공고시 품명",
			"상품정보제공고시 모델명", "상품정보제공고시 인증허가사항", "상품정보제공고시 제조자" 
	};
	
	public ExcelOutputNaver(ExcelInput input, LinkedList<Excel> excel, Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		this.fileName = "네이버_"+input.getSearchWord()+"_"+format.format(date)+".xls";
		this.excel = excel;
		this.input = input;
	}
	
	public void makeExcelOutputNaver(String folderName){
		System.out.println("엑셀 파일 생성 중...");
		int count = 1, folderCount = 1;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		
		for(int i=0;i<menu.length;i++)
			row.createCell(i).setCellValue(menu[i]);
		CellStyle cs = workbook.createCellStyle();
		cs.setWrapText(true);
		for(int i=0;i<excel.size();i++){
			row = sheet.createRow(i+1);
			Excel tmpExcel = excel.get(i);
			LinkedList<String> tmpChoiceList = tmpExcel.getChoiceList();
 
			String option = "";
			String optionValue = "";
			for(int j=0;j<tmpChoiceList.size();j++){
				String list = tmpChoiceList.get(j);
				option += list;
				optionValue += tmpExcel.getChoiceListHashMap().get(list);
				if(j!=tmpChoiceList.size()-1){
					option += "\n";
					optionValue += "\n";
				}
			}
			optionValue = optionValue.replace('[', ' ');
			optionValue = optionValue.replace(']', ' ');
			optionValue = optionValue.replaceAll(" ", "");
			
			row.createCell(0).setCellValue("새상품");
			row.createCell(1).setCellValue(input.getCategory());
			row.createCell(3).setCellValue("["+input.getHeader()+"]"+tmpExcel.getTitle());
			row.createCell(5).setCellValue(getCalculatorPrice(tmpExcel.getPrice()));
			row.createCell(6).setCellValue("1000");
			row.createCell(7).setCellValue("배송기간은 15~30일 입니다");
			row.createCell(8).setCellValue(input.getPhone());
			row.createCell(9).setCellValue(tmpExcel.getProductID()+".jpg");
			row.createCell(15).setCellValue(tmpExcel.getDetail());
			row.createCell(23).setCellValue("과세상품");
			row.createCell(24).setCellValue("Y");
			row.createCell(25).setCellValue("Y");
			row.createCell(26).setCellValue("N");
			row.createCell(27).setCellValue("0200037");
			row.createCell(28).setCellValue("중국,홍콩");
			row.createCell(29).setCellValue("Y");
			row.createCell(31).setCellValue("택배, 소포, 등기");
			row.createCell(32).setCellValue("무료");
			row.createCell(37).setCellValue(input.getTakeback());
			row.createCell(38).setCellValue(input.getExchange());
			row.createCell(55).setCellValue("단독형");
			row.createCell(56).setCellStyle(cs);
			row.createCell(56).setCellValue(option);
			row.createCell(57).setCellStyle(cs);
			row.createCell(57).setCellValue(optionValue);
		}
			
		try {
			FileOutputStream outFile = new FileOutputStream(folderName+"\\"+this.fileName);
			workbook.write(outFile);
			outFile.close();
			workbook.close();
			System.out.println(this.fileName+" 파일 생성 완료");
		} catch (Exception e) {
			System.out.println("엑셀 파일 실패");
		}
	}

	private String getCalculatorPrice(double price) {
		if(price>=3000000)
			price += price*0.6;
		else if(price>=200000)
			price += price*0.8;
		else if(price>=100000)
			price += price*1;
		else if(price>=50000)
			price += price*1.05;
		else if(price>=30000)
			price += price*1.2;
		else if(price>=15000)
			price += price*1.3;
		else
			price += (price+18000);
		int tmpPrice = (int)price;
		return Integer.toString(((tmpPrice/100)*100)*input.getDollar());
	}
}
