package com.chinasoft.wt.service;

import com.chinasoft.wt.model.WorkTimeRecord;
import com.chinasoft.wt.repository.WorkTimeRecordRepository;
import com.chinasoft.wt.util.DateUtils;
import com.chinasoft.wt.util.ExcelUtils;
import com.chinasoft.wt.vo.SummaryVO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
@Service
public class WorkTimeRecordService {
    @Autowired
    private ExcelUtils excelUtils;
    @Autowired
    private WorkTimeRecordRepository workTimeRecordRepository;

    //上班时间
    private Date nineOneAmClock=DateUtils.parseStrToDate("9:01",DateUtils.DATE_FORMAT_HHMI);
    //下班时间
    private Date fiveHalfPmClock=DateUtils.parseStrToDate("17:30",DateUtils.DATE_FORMAT_HHMI);
    //下午6点以后上班,有秒时采取舍掉方式
    private Date sixPmClock=DateUtils.parseStrToDate("18:01",DateUtils.DATE_FORMAT_HHMI);
    //用于根据毫秒计算分钟的分母
    private static final long CAU_MIN_BY=1000*60;
    /**
     *
     * 导入工时excel
     *
     * **/
    public int importWTRfromXLS(String xlsPath) throws Exception {
        //1. 解析xls
        InputStream    ins = new FileInputStream(
                    new File(xlsPath));
        List<WorkTimeRecord>    wtrList = excelUtils.readXlsxFileToObj(ins);

        //2. 插入数据库
        return workTimeRecordRepository.save(wtrList).size();
    }

    /**
     *
     * 查询所有的工时信息
     *
     * **/
    public java.util.List<WorkTimeRecord> findAll() {
        return workTimeRecordRepository.findAll();
    }


    /**
     *
     * 计算实际上班时长
     *
     * **/
    public double caculateAcutalLength() {
         //1. 计算全部为正常上下班的情况，即9点以前上班含九点，5点半以后含5点半
         //(1) 先处理6点以前下班的情况，判断上班时间早于9点01，下班时间大于5点半小于6点的
         // 算法 下班时间-上班时间-1.5(午餐时间)=实际上班时长
         //(2) 处理6点以后下班的，判断上班时间早于9点01，下班时间大于6点的
         // 算法 下班时间-上班时间-2(午餐时间+晚餐时间)=实际上班时长
         //(3) 存在迟到或者早退的，这种后续处理
        List list = this.findAll();
        double acutalTime=0.0;
        for(Iterator<WorkTimeRecord> it = list.iterator();it.hasNext();){
            WorkTimeRecord wtr = it.next();
            Date startTime = DateUtils.parseStrToDate(wtr.getStartTime(),DateUtils.DATE_FORMAT_HHMI);
            Date endTime = DateUtils.parseStrToDate(wtr.getEndTime(),DateUtils.DATE_FORMAT_HHMI);
            double acutalTimeDay=0.0;
            if(startTime.getTime()<nineOneAmClock.getTime()
                    &&endTime.getTime()>fiveHalfPmClock.getTime()
                    &&endTime.getTime()<sixPmClock.getTime()
                    ){//9点以前上班含九点，下午 5点半以后，6点以前下班的
                // 算法 下班时间-上班时间-1.5(午餐时间)=实际上班时长
                long millsecondTime = endTime.getTime()-startTime.getTime();//毫秒
                acutalTimeDay=(millsecondTime/CAU_MIN_BY)-1.5*60;
            }else if(startTime.getTime()<nineOneAmClock.getTime()
                    &&endTime.getTime()>sixPmClock.getTime()
                    ){//9点以前上班含九点， 6点以后下班的
                // 算法 下班时间-上班时间-1.5(午餐时间)=实际上班时长
                long millsecondTime = endTime.getTime()-startTime.getTime();//毫秒
                acutalTimeDay=(millsecondTime/CAU_MIN_BY)-2*60;
            }
            acutalTime=acutalTime+acutalTimeDay;//对每天的上班时间进行累加
        }
        return acutalTime;

    }

    /***
     * 汇总
     * **/
    public SummaryVO summary() {
        SummaryVO vo =new SummaryVO();
        long expectWTL = findAll().size()*8*60;
        double actWTL = this.caculateAcutalLength();
        long alvTWL = new Double(actWTL).longValue() - expectWTL;
        vo.setActWTL(actWTL+"");
        vo.setAlvTWL(alvTWL+"");
        vo.setExpectWTL(expectWTL+"");
        return vo;
    }
}
