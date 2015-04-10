package com.poicom.common.kit.excel;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class PoiKit {
	private static POIFSFileSystem fs; 
	private static HSSFWorkbook wb;
	private static HSSFSheet sheet; 
	private static HSSFRow row; 
	
	private static String[] headers;
	
	/**
	 * 读取Excel表格表头的内容
	 * 
	 * @param InputStream
	 * @return String 表头内容的数组
	 */
	@SuppressWarnings("deprecation")
	public static String[] readHeader(InputStream is){
		try{
			fs=new POIFSFileSystem(is);
			wb=new HSSFWorkbook(fs);
		}catch(IOException e){
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		headers = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			headers[i] = getCellFormatValue(row.getCell((short) i));
		}
		return headers;
		
	}
	
	/**
	 * 读取Excel数据内容
	 * 
	 * @param InputStream
	 * @return Map 包含单元格数据内容的Map对象
	 */
	@SuppressWarnings("deprecation")
	public static Map<Integer,String> readContent(InputStream is){
		Map<Integer, String> content = new HashMap<Integer, String>(); // 用来获取单个Excel文件内容
		StringBuffer str=new StringBuffer();
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		
		for(int i=1;i<=rowNum;i++){
			row=sheet.getRow(i);
			//
			int colNum=row.getPhysicalNumberOfCells();
			int j=0;
			while(j<colNum){
				str.append(getCellFormatValue(row.getCell((short) j)).trim()+"/");
				j++;
			}
			content.put(i, str.toString());
			str.delete(0,str.length()-1);
		}
		
		return content;
	}
	
	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellFormatValue(HSSFCell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case HSSFCell.CELL_TYPE_NUMERIC:
			case HSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);

				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			// 如果当前Cell的Type为STRING
			case HSSFCell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}
	
	
	

}
