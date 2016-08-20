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
			"��ǰ����", "ī�װ�ID", "ī�װ� �Ӽ� �ڵ�", "��ǰ��", "ȫ������",
			"�ǸŰ�", "������", "A/S �ȳ�����", "A/S ��ȭ��ȣ", "��ǥ �̹��� ���ϸ�",
			"����Ʈ�� �̹��� ���ϸ�", "�̹����� �̹��� ���ϸ�", "ū�̹����� �̹��� ���ϸ�", "�������� �̹��� ���ϸ�", "�߰� �̹��� ���ϸ�",
			"��ǰ ������", "�Ǹ��� ��ǰ�ڵ�", "�Ǹ��� ���ڵ�", "������", "�귣��",
			"�� �ڵ�", "��������", "��ȿ����", "�ΰ���", "�̼����� ����",
			"������ ���⿩��", "�������ȸ�� ���뿩��", "������ �ڵ�", "���Ի�", "���������� ����", 
			"������ �����Է�", "��۹��", "��ۺ� ����", "�⺻��ۺ�", "��ۺ� �������", 
			"���Ǻι���-��ǰ�ǸŰ��հ�", "�������ΰ�-����", "��ǰ��ۺ�", "��ȯ��ۺ�", "������ �����ۺ� ����", 
			"������ġ��	", "�Ǹ��� Ư�̻���", "������� ��", "������� ����", "������������ ���� ��",
			"������������ ���� ����", "������������ ��", "������������ ����", "��ǰ���Ž� ����Ʈ ���� ��", "��ǰ���Ž� ����Ʈ ���� ����",
			"������ �ۼ��� ���� ����Ʈ", "�����̾� ������ �ۼ��� ���� ����Ʈ", "�������ȸ�� �߰� ���� ����Ʈ", "������ �Һ� ����", "����ǰ",
			"�ɼ�����", "�ɼǸ�", "�ɼǰ�", "�ɼǰ�", "�ɼ� ������",
			"�߰���ǰ��", "�߰���ǰ��", "�߰���ǰ��", "�߰���ǰ ������", "��ǰ����������� ǰ��",
			"��ǰ����������� �𵨸�", "��ǰ����������� �����㰡����", "��ǰ����������� ������" 
	};
	
	public ExcelOutputNaver(ExcelInput input, LinkedList<Excel> excel, Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		this.fileName = "���̹�_"+input.getSearchWord()+"_"+format.format(date)+".xls";
		this.excel = excel;
		this.input = input;
	}
	
	public void makeExcelOutputNaver(String folderName){
		System.out.println("���� ���� ���� ��...");
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
			
			row.createCell(0).setCellValue("����ǰ");
			row.createCell(1).setCellValue(input.getCategory());
			row.createCell(3).setCellValue("["+input.getHeader()+"]"+tmpExcel.getTitle());
			row.createCell(5).setCellValue(getCalculatorPrice(tmpExcel.getPrice()));
			row.createCell(6).setCellValue("1000");
			row.createCell(7).setCellValue("��۱Ⱓ�� 15~30�� �Դϴ�");
			row.createCell(8).setCellValue(input.getPhone());
			row.createCell(9).setCellValue(tmpExcel.getProductID()+".jpg");
			row.createCell(15).setCellValue(tmpExcel.getDetail());
			row.createCell(23).setCellValue("������ǰ");
			row.createCell(24).setCellValue("Y");
			row.createCell(25).setCellValue("Y");
			row.createCell(26).setCellValue("N");
			row.createCell(27).setCellValue("0200037");
			row.createCell(28).setCellValue("�߱�,ȫ��");
			row.createCell(29).setCellValue("Y");
			row.createCell(31).setCellValue("�ù�, ����, ���");
			row.createCell(32).setCellValue("����");
			row.createCell(37).setCellValue(input.getTakeback());
			row.createCell(38).setCellValue(input.getExchange());
			row.createCell(55).setCellValue("�ܵ���");
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
			System.out.println(this.fileName+" ���� ���� �Ϸ�");
		} catch (Exception e) {
			System.out.println("���� ���� ����");
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
