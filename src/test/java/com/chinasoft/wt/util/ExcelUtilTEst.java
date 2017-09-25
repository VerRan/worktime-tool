package com.chinasoft.wt.util;

import com.chinasoft.wt.model.WorkTimeRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExcelUtilTEst.class)
@ComponentScan("com.chinasoft")
public class ExcelUtilTEst {

    @Autowired
    private ExcelUtils excelUtils;

    @Test
    public void readFromXlsx() throws Exception{

        InputStream ins =new FileInputStream(
                new File("f://test.xlsx"));
        List<WorkTimeRecord> wtrList = excelUtils.readXlsxFileToObj(ins);
        Assert.assertEquals(11,wtrList.size());
    }



//    public void testRead() throws Exception{
//        InputStream ins =new FileInputStream(
//                new File("f://test.xlsx"));
//        //excel 文件
//        Workbook wb= WorkbookFactory.create(ins);
//        ins.close();
//        //sheet 获取第一个sheet
//        Sheet sheet =  wb.getSheetAt(0);
//        //获取表头第一行
//        Row row =   sheet.getRow(0);
//
//        //获取第一个单元格
//        Assert.assertEquals("工号",row.getCell(0).getStringCellValue());
//
//        for(int i=0;i<sheet.getLastRowNum();i++){
//            Row subRow =   sheet.getRow(i);
//            if(i==0){//第一行为string
//                System.out.println(subRow.getCell(0).getStringCellValue());//打印第一列的数据
//            }else{
//                System.out.println(Math.round(subRow.getCell(0).getNumericCellValue()));//打印第一列的数据
//            }
//
//        }
//    }


    //HSSFWorkbook 适用于 xls 文件

//    @Test
//    public void readFromXls() throws Exception{
//        //excel 文件
//        Workbook wb=new HSSFWorkbook(
//                new FileInputStream(
//                new File("f://test.xls")));
//        //sheet 获取第一个sheet
//        Sheet sheet =  wb.getSheetAt(0);
//
//        //获取表头第一行
//        Row row =   sheet.getRow(0);
//
//        //获取第一个单元格
//        Assert.assertEquals("工号",row.getCell(0).getStringCellValue());
//    }

    //XSSF 适用于 xlsx

}
