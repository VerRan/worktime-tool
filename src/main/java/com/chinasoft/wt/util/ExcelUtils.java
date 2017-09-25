package com.chinasoft.wt.util;

import com.chinasoft.wt.model.WorkTimeRecord;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
@Component
public class ExcelUtils {
    public List<WorkTimeRecord> readXlsxFileToObj(InputStream ins) throws IOException, InvalidFormatException {
        List<WorkTimeRecord> list =new ArrayList();

        //excel 文件
        Workbook wb= WorkbookFactory.create(ins);
        ins.close();
        //sheet 获取第一个sheet ，默认用户应该会直接复制到sheet1中
        Sheet sheet =  wb.getSheetAt(0);
        //循环获取每一行数据
        for(int i=0;i<sheet.getLastRowNum()+1;i++){
            Row subRow =   sheet.getRow(i);
            if(i==0){//第一行直接过滤不进行处理
                continue;
            }
            WorkTimeRecord wtr=new WorkTimeRecord();
            //循环每一列
            for(int j=0;j<subRow.getLastCellNum();j++){
                    if(j==2||j==3){//2,3 管辖地域	当前地域列不处理
                        continue;
                    }
                // excel 列说明 工号	姓名	管辖地域	当前地域	打卡日期	首次打卡时间	末次打卡时间
                    Cell cell =subRow.getCell(j);

                    if(j==0){//第一列 工号数字型
                        wtr.setStaffCode(cell.getNumericCellValue()+"");
                    }
                    if(j==1){//姓名
                        wtr.setStaffName(cell.getStringCellValue()+"");
                    }

                    if(j==4){//打卡日期
                        Date date = XSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                        wtr.setWokeDate(DateUtils.parseDateToStr(date,DateUtils.DATE_FORMAT_YYYYMMDD));
                    }

                    if(j==5){//首次打卡时间
                        Date date = XSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                        wtr.setStartTime(DateUtils.parseDateToStr(date,DateUtils.DATE_FORMAT_HHMI));
                    }

                    if(j==6){//末次打卡时间
                        Date date = XSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                        wtr.setEndTime(DateUtils.parseDateToStr(date,DateUtils.DATE_FORMAT_HHMI));
                    }

            }

            list.add(wtr);
        }
        return list;
    }
}
