package com.ck.furn.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.enums.EnumUtils;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ck.furn.sys.bean.User;
import com.ck.furn.util.DateUtils;
import com.ck.furn.util.IdGen;
import com.ck.furn.util.MD5Util;

/**
 * excel工具类
 * @author ck
 *
 */
public class PoiUtils {

    public static ResponseEntity<byte[]> exportEmp2Excel(List<User> users) {
        HttpHeaders headers = null;
        ByteArrayOutputStream baos = null;
        try {
            //1.创建Excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            //2.创建文档摘要
            workbook.createInformationProperties();
            //3.获取文档信息，并配置
            DocumentSummaryInformation dsi = workbook.getDocumentSummaryInformation();
         
            //4.获取摘要信息并配置
            SummaryInformation si = workbook.getSummaryInformation();
            //4.2.设置文档标题
            si.setTitle("用户信息");
            //创建Excel表单
            HSSFSheet sheet = workbook.createSheet("用户信息表");
            //创建日期显示格式
            HSSFCellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
            //创建标题的显示样式
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //定义列的宽度
            sheet.setColumnWidth(0, 35 * 256);
            sheet.setColumnWidth(1, 22 * 256);
            sheet.setColumnWidth(2, 15 * 256);
            sheet.setColumnWidth(3, 15 * 256);
            sheet.setColumnWidth(4, 22 * 256);
            //5.设置表头
            HSSFRow headerRow = sheet.createRow(0);
            HSSFCell cell0 = headerRow.createCell(0);
            cell0.setCellValue("Id");
            cell0.setCellStyle(headerStyle);
            HSSFCell cell1 = headerRow.createCell(1);
            cell1.setCellValue("姓名");
            cell1.setCellStyle(headerStyle);
            HSSFCell cell2 = headerRow.createCell(2);
            cell2.setCellValue("登录名");
            cell2.setCellStyle(headerStyle);
            HSSFCell cell3 = headerRow.createCell(3);
            cell3.setCellValue("角色类型");
            cell3.setCellStyle(headerStyle);
            HSSFCell cell4 = headerRow.createCell(4);
            cell4.setCellValue("手机号");
            cell4.setCellStyle(headerStyle);
            HSSFCell cell5 = headerRow.createCell(5);
            cell5.setCellValue("性别");
            cell5.setCellStyle(headerStyle);
            HSSFCell cell6 = headerRow.createCell(6);
            cell6.setCellValue("是否封禁");
            cell6.setCellStyle(headerStyle);
            HSSFCell cell7 = headerRow.createCell(7);
            cell7.setCellValue("注册时间");
            cell7.setCellStyle(headerStyle);
            //6.装数据
            for (int i = 0; i < users.size(); i++) {
                HSSFRow row = sheet.createRow(i + 1);
                User user = users.get(i);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getName());
                row.createCell(2).setCellValue(user.getLoginName());
                row.createCell(3).setCellValue(user.getRoleType());
                row.createCell(4).setCellValue(user.getPhone());
                row.createCell(5).setCellValue(user.getSex());
                row.createCell(6).setCellValue(user.getKeepAlive());
                row.createCell(7).setCellValue(user.getCreateTime());
            }
            headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment",
                    new String("用户信息表.xls".getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            baos = new ByteArrayOutputStream();
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    public static List<User> importEmp2List(MultipartFile file) {
        List<User> users = new ArrayList<User>();
        try {
        	HSSFWorkbook workbook =
                new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
        	int numberOfSheets = workbook.getNumberOfSheets();
	        for (int i = 0; i < numberOfSheets; i++) {
		        HSSFSheet sheet = workbook.getSheetAt(i);
		        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
		        User user;
		        for (int j = 0; j < physicalNumberOfRows; j++) {
			        if (j == 0) {
			            continue;//标题行
			        }
			        HSSFRow row = sheet.getRow(j);
			        if (row == null) {
			            continue;//没数据
			        }
			        int physicalNumberOfCells = row.getPhysicalNumberOfCells();
			        user = new User();
			        for (int k = 0; k < physicalNumberOfCells; k++) {
				        HSSFCell cell = row.getCell(k);
				        if(cell == null) {
				        	continue;
				        }
				        System.out.println(cell.getCellTypeEnum());
			            String cellValue = "";
				        switch (row.getCell(k).getCellType()) {
						case Cell.CELL_TYPE_BOOLEAN:
							break;
						case Cell.CELL_TYPE_NUMERIC:
							cell.setCellType(CellType.STRING);  
							cellValue = cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_STRING:
							cellValue = cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_BLANK:
							break;
						case Cell.CELL_TYPE_ERROR:
							break;
						case Cell.CELL_TYPE_FORMULA:
							break;
						}
				        
			            if (cellValue == null) {
			                cellValue = "";
			            }
			            switch (k) {
			                case 0:
			                    user.setName(cellValue);
			                    break;
			                case 1:
			                    user.setLoginName(cellValue);
			                    break;
			                case 2:
			                	String passWord = MD5Util.getMD5(cellValue);
			                    user.setPassWord(passWord);
			                    break;
			                case 3:
			                    user.setRoleType(cellValue);
			                    break;
			                case 4:
			                    user.setSex(cellValue);
			                    break;
			                case 5:
			                    user.setPhone(cellValue);
			                    break;
			            }
			        }
					user.setCreateTime(DateUtils.getDateTime());
					user.setUpdateTime(DateUtils.getDateTime());
		        	users.add(user);
		        }
	        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
