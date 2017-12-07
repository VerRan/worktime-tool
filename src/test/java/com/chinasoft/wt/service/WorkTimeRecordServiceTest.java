package com.chinasoft.wt.service;

import com.chinasoft.wt.model.WorkTimeRecord;
import com.chinasoft.wt.util.ExcelUtils;
import com.chinasoft.wt.vo.SummaryVO;
import org.junit.Assert;
import org.junit.Before;
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
import java.util.Map;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkTimeRecordServiceTest.class)
@ComponentScan("com.chinasoft")
public class WorkTimeRecordServiceTest {
    @Autowired
    private   WorkTimeRecordService workTimeRecordService;


    @Before
    public void testImportWTRfromXLS() throws Exception {
        String xlsPath="f://test.xlsx";
        int recordCount = workTimeRecordService.importWTRfromXLS(xlsPath,"107207");
        Assert.assertEquals(11,recordCount);

    }
//
//    @Test
//    ///计算应上班时长,算法应该为总记录数*每天规定时长
//    //针对迟到等情况先不处理，假设均未迟到
//    public void testCaculateExpectedLength(){
//        List list = workTimeRecordService.findAll();
//        Assert.assertEquals(11,list.size());
//        Assert.assertEquals(88,list.size()*8);
//    }
//
//    @Test
//    //计算实际上班时长
//    public void testCaculateAcutalLength(){
//        List list = workTimeRecordService.findAll();
//        //单位小时
//       double wtr= workTimeRecordService.caculateAcutalLength(list);
//        System.out.println("AcutalLength is "+wtr);
//    }

    @Test
    //计算可用抵扣时长
    public  void testCaculateAvaliableLength(){
        List list = workTimeRecordService.findAll();
        double wtr= workTimeRecordService.caculateAcutalLength(list);
        System.out.println("wtr is "+wtr+"分钟");
        System.out.println("act is "+88*60+"分钟");
        System.out.println("avilable is "+(wtr-88*60)+"分钟");
        Assert.assertEquals(true,(wtr-88*60)>0);//工时足够
    }

    @Test
    //汇总
    public void testSummary(){
        SummaryVO summaryVO = workTimeRecordService.summary("107207");
    }
}
