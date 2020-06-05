package com.fx.commons.utils.other.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 读写EXCEL文件
 */
public class POIUtils {
	
	
	private final String position_title = "title";
	private final String position_body = "body";
	
	
	/**
	 * 判断excel版本
	 * @param in
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private Workbook openWorkbook(InputStream in, String filename)
			throws IOException {
		Workbook wb = null;
		if (filename.endsWith(".xlsx")) {
			wb = new XSSFWorkbook(in);// Excel 2007
		} else {
			wb = new HSSFWorkbook(in);// Excel 2003
		}
		return wb;
	}

	/**
	 * 根据文件路径和工作薄下标导入Excel数据
	 * @param file 路径
	 * @param fileName 文件名
	 * @param sheetIndex 工作薄下标
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> getExcelData(File file , String fileName,int sheetIndex,int startRow) throws Exception {
		List<List<String>> dataLst = new ArrayList<List<String>>();
		// 构造 Workbook 对象，strPath 传入文件路径
		FileInputStream inputStream = null;
		inputStream = new FileInputStream(file);
		Workbook wb = openWorkbook(inputStream, fileName);
		// 读取第一章表格内容
		Sheet sheet = (Sheet) wb.getSheetAt(sheetIndex);// 切换工作薄
		Row row = null;
		Cell cell = null;

		int totalRows = sheet.getLastRowNum();
		/** 得到Excel的列数 */
//		int totalCells = totalRows >= 1 && sheet.getRow(0) != null ? sheet.getLastRowNum() : 0;//这里只取第一行只能导入无表头的数据
//		System.out.println(totalCells);
		for (int r = startRow; r <=totalRows; r++) {
			row = sheet.getRow(r);
			if (row == null || curRowInsideNull(row, row.getPhysicalNumberOfCells()))
				continue;
			List<String> rowLst = new ArrayList<String>();
			 //获得当前行的开始列
            int firstCellNum = row.getFirstCellNum();
            //获得当前行的列数
            int lastCellNum = row.getLastCellNum();//获取最后一个不为空的单元格;getPhysicalNumberOfCells获取不为空单元格的个数
			for (int c = firstCellNum; c <= lastCellNum; c++) {
				cell = row.getCell(c);
				String cellValue = "";
				if (null != cell) {
					// 以下是判断数据的类型
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC: // 数字
						int cellStyle = cell.getCellStyle().getDataFormat();
						String cellStyleStr = cell.getCellStyle().getDataFormatString();
						if ("0.00_);[Red]\\(0.00\\)".equals(cellStyleStr)) {
							NumberFormat f = new DecimalFormat("#.##");
							cellValue = (f.format((cell.getNumericCellValue())) + "")
									.trim();
						} else if (HSSFDateUtil.isCellDateFormatted(cell)) {
							/*cellValue = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();*/
							 SimpleDateFormat sdf = null;  
				                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {  
				                    sdf = new SimpleDateFormat("HH:mm");  
				                } else {// 日期  
				                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				                }  
				                Date date = cell.getDateCellValue();  
//				                System.out.println(date); 
				                cellValue = sdf.format(date); 
//				                System.out.println("cellValue"+cellValue);        
						} else if ( cellStyle == 58 || cellStyle == 179 || "m\"月\"d\"日\";@".equals(cellStyleStr)) {
							// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							double value = cell.getNumericCellValue();
							Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
							cellValue = sdf.format(date);
//						} else if ((cellStyle == 181 || cellStyle == 177|| cellStyle == 176)&& cellStyleStr.endsWith("@")) { 
							//星期几 Excel中的日期自定义格式 cellStyle不固定，故采用  "[$-804]aaaa;@"来判断							
						} else if ("[$-804]aaaa;@".equals(cellStyleStr)) {
							SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
							double value = cell.getNumericCellValue();
							Date date = org.apache.poi.ss.usermodel.DateUtil
									.getJavaDate(value);
							cellValue = sdf.format(date);

						} else {
							NumberFormat f = new DecimalFormat("#.##");
							cellValue = (f.format((cell.getNumericCellValue())) + "").trim();
						}
						break;
					case HSSFCell.CELL_TYPE_STRING: // 字符串
						cellValue = cell.getStringCellValue();
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
						cellValue = cell.getBooleanCellValue() + "";
						break;
					case HSSFCell.CELL_TYPE_FORMULA: // 公式
						try {
							cellValue = String.valueOf(cell.getNumericCellValue());
						} catch (IllegalStateException e) {
							try {
								cellValue = String.valueOf(cell.getRichStringCellValue());
							} catch (Exception e1) {
								cellValue="";
							}
						}
						break;
					case HSSFCell.CELL_TYPE_BLANK: // 空值
						//cellValue = "";
						break;

					case HSSFCell.CELL_TYPE_ERROR: // 故障
						//cellValue = "非法字符";
						break;
					default:
						//cellValue = "未知类型";
						break;
					}
				}
				rowLst.add(cellValue);
			}
			dataLst.add(rowLst);
		}
		return dataLst;
	}

	/**
	 * 判断当前行内所有单元格是否为空
	 * 
	 * @param row
	 * @param totalCells
	 * @return
	 */
	private boolean curRowInsideNull(Row row, int totalCells) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < totalCells; i++) {
			row.getCell(i, HSSFRow.RETURN_BLANK_AS_NULL);
			Cell cell = row.getCell(i, HSSFRow.RETURN_BLANK_AS_NULL);
			if (cell != null) {
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					sb.append(cell.getStringCellValue().trim());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					sb.append(String.valueOf(cell.getNumericCellValue()));
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					sb.append(String.valueOf(cell.getBooleanCellValue()));
					break;
				case Cell.CELL_TYPE_FORMULA://判断公式生成的结果
					String value = "";
					try {
						value = String.valueOf(cell.getNumericCellValue());
					} catch (IllegalStateException e) {
						try {
							value = String.valueOf(cell.getRichStringCellValue());
						} catch (Exception e1) {
							value="";
						}
					}
					sb.append(value);
					break;
				default:
					break;
				}
			}
		}
		if (sb.toString().trim().equals(""))
			return true;
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public HSSFWorkbook handleDataToExcel(List list,Class clazz,String sheetName,int pageSize) throws Exception{
		
		HSSFWorkbook workbook = null;
		workbook = new HSSFWorkbook();
		// 获取Excel标题
		List<ExcelHeader> headers = getHeaderList(clazz);
		Collections.sort(headers);
		// 
		if(null != list && list.size() > 0 ){
			int sheetCount = list.size() % pageSize == 0 ? list.size() / pageSize : list.size() / pageSize + 1;
			for(int i = 1; i <= sheetCount; i++){
				HSSFSheet sheet = null;
				if(!StringUtils.isEmpty(sheetName)){
					sheet = workbook.createSheet(sheetName + i);
				}else{
					sheet = workbook.createSheet();
				}
				
				HSSFRow row = sheet.createRow(0);
				// 写标题
				CellStyle titleStyle = setCellStyle(workbook,position_title);
				for(int j = 0; j < headers.size();j++){
					HSSFCell cell = row.createCell(j);
					cell.setCellStyle(titleStyle);
					cell.setCellValue(headers.get(j).getTitle());
					sheet.setColumnWidth(j, headers.get(j).getWidth()*256);
				}
				
				// 写内容
				Object obj = null;
				CellStyle bodyStyle = setCellStyle(workbook, position_body);
				int begin = (i - 1) * pageSize;
				int end = (begin + pageSize) > list.size() ? list.size() : (begin + pageSize);
				int rowCount = 1;
				for(int n = begin; n < end; n++){
					row = sheet.createRow(rowCount);
					rowCount++;
					obj = list.get(n);
					for(int x = 0; x < headers.size(); x++){
						Cell cell = row.createCell(x);
						cell.setCellStyle(bodyStyle);
						@SuppressWarnings("unchecked")
						Method method = clazz.getDeclaredMethod(headers.get(x).getMethodName());
						Object value = method.invoke(obj);
						if(value instanceof Date){
							 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							 String formattedDate = dateFormat.format(new Date());
							 cell.setCellValue((String)formattedDate);
						}else if(value instanceof Double){
							cell.setCellValue((Double)value);
						}else if(value instanceof String){
							cell.setCellValue((String)value);								
							/*if(((String)value).trim().length()>0 && isNumeric((String)value)){
								cell.setCellType(Cell.CELL_TYPE_STRING);		
								cell.setCellValue((String)value);
							}else{
							}*/
						}else if(value instanceof Integer){
							cell.setCellValue((Integer) value);
						}
						
//						cell.setCellValue(org.apache.commons.beanutils.BeanUtils.getProperty(obj,getPropertyName(headers.get(x))));
					}
				}
			}
		}		
		return workbook;
	}
	
	/**
	 * 注解形式导出Excel
	 * @author lanyuan
	 * @Email：mmm333zzz520@163.com
	 * @date：2014-4-18
	 * @param response HttpServletResponse
	 * @param fileName 导出Excel的文件名
	 * @param List<T> objs 某一个实体类数据集合
	 * @param clazz <T> 某一个实体类
	 * @param sheetName sheet的名称
	 * @param pageSize sheet显示多少条数据
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void exportToExcel(HttpServletResponse response,String fileName,List objs, Class clazz,
			String sheetName, int pageSize){
		OutputStream out = null;
		try {
			String tempName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("content-disposition", "attachment;filename=" + tempName + ".xls");
			response.setContentType("application/vnd.ms-excel");
			HSSFWorkbook workbook = handleDataToExcel(objs, clazz, "sheet", pageSize);
			out = response.getOutputStream();
			workbook.write(out);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * 根据方法获取字段名
	 * @param excelHeader
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getPropertyName(ExcelHeader excelHeader) {
		String temp = excelHeader.getMethodName().substring(3);
		return temp.substring(0, 1).toLowerCase() + temp.substring(1);
	}

	/**
	 * 设置表格样式
	 * @param workbook
	 * @param position
	 * @return
	 */
	private CellStyle setCellStyle(HSSFWorkbook workbook,
			String position) {
		
		CellStyle style = workbook.createCellStyle();
		// 设置单元格字体水平、垂直居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 设置单元格边框
		style.setBorderBottom((short)1);
		style.setBorderLeft((short)1);
		style.setBorderRight((short)1);
		style.setBorderTop((short)1);
		// 设置单元格字体
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		if(position_title.equals(position)){
			font.setFontHeightInPoints((short)11);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}else{
			font.setFontHeightInPoints((short)10);
		}
		style.setFont(font);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index); 
		style.setWrapText(true);
		return style;
	}

	/**
	 * 获取excel标题列表
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<ExcelHeader> getHeaderList(Class clazz) {
		
		List<ExcelHeader> headers = new ArrayList<ExcelHeader>();
		java.lang.reflect.Method [] ms = clazz.getDeclaredMethods();
		for(java.lang.reflect.Method m : ms){
			String mn = m.getName();
			if(mn.startsWith("get")){
				if(m.isAnnotationPresent(ExcelDataMapper.class)){
					ExcelDataMapper dataMapper = m.getAnnotation(ExcelDataMapper.class);
					headers.add(new ExcelHeader(dataMapper.title(), dataMapper.order(),dataMapper.width(), mn));
				}
			}
		}
		return headers;
	}
	
	
	
	/**
     * excel导出数据
     * @author xx
     * @date 20200520
     * @param title    显示的导出表的标题
     * @param rowName  导出表的列名
     * @param dataList 表的内容
     * @return
     */
	public  HSSFWorkbook poiExport(String title, String[] rowName, List<Object[]> dataList) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
        HSSFSheet sheet = workbook.createSheet(title); // 创建工作表
        // 产生表格标题行
        HSSFRow rowm = sheet.createRow(0);
        HSSFCell cellTiltle = rowm.createCell(0);
        // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
        HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);// 获取列头样式对象
        HSSFCellStyle style = getStyle(workbook); // 单元格样式对象
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length - 1)));
        cellTiltle.setCellStyle(columnTopStyle);
        cellTiltle.setCellValue(title);
        // 定义所需列数
        int columnNum = rowName.length;
        HSSFRow rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)
        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum; n++) {
            HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
            cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
            HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
            cellRowName.setCellValue(text); // 设置列头单元格的值
            cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式

        }
        // 将查询出的数据设置到sheet对应的单元格中
        for (int i = 0; i < dataList.size(); i++) {
            Object[] obj = dataList.get(i);// 遍历每个对象
            HSSFRow row = sheet.createRow(i + 3);// 创建所需的行数
            for (int j = 0; j < obj.length; j++) {
                HSSFCell cell = null; // 设置单元格的数据类型
                if (j == 0) {
                    cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(i + 1);
                } else {
                    cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                    if (!"".equals(obj[j]) && obj[j] != null) {
                        cell.setCellValue(obj[j].toString()); // 设置单元格的值
                    }
                }
                cell.setCellStyle(style); // 设置单元格样式
            }
        }
        // 让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if (colNum == 0) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }
        }
        return workbook;
    }

    /**
     * 列头单元格样式
     *
     * @param workbook
     * @return
     */
    public  HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    /**
     	* 列数据信息单元格样式
     *
     * @param workbook
     * @return
     */
    public  HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }
    /**
          * 下载输出excel
     * @param response
     * @param workbook
     * @param fileName
     */
	public void downToExcel(HttpServletResponse response,HSSFWorkbook workbook,String fileName){
		OutputStream out = null;
		try {
	        response.setContentType("application/octet-stream;charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment;filename="
	                + new String(fileName.getBytes(), StandardCharsets.ISO_8859_1) + ".xls");
	        out = response.getOutputStream();
	        workbook.write(out);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
			}
		}
	}
}