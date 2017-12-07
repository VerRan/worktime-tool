package com.chinasoft.wt.service;

import com.chinasoft.wt.model.WorkTimeRecord;
import com.chinasoft.wt.repository.WorkTimeRecordRepository;
import com.chinasoft.wt.util.*;
import com.chinasoft.wt.vo.SummaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
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
    public int importWTRfromXLS(String xlsPath,String staffCode) throws Exception {
        //1. 解析xls
        InputStream    ins = new FileInputStream(
                    new File(xlsPath));

        //2. 每次导入 先清除当前登陆工号的数据，避免重复
        WorkTimeRecord workTimeRecord=new WorkTimeRecord();
        workTimeRecord.setStaffCode(staffCode);
        workTimeRecordRepository.delete(workTimeRecord);

        //3. 导入文件信息
        List<WorkTimeRecord>    wtrList = excelUtils.readXlsxFileToObj(ins);

        //4. 插入数据库
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
    public double caculateAcutalLength(List<WorkTimeRecord> list) {
         //1. 计算全部为正常上下班的情况，即9点以前上班含九点，5点半以后含5点半
         //(1) 先处理6点以前下班的情况，判断上班时间早于9点01，下班时间大于5点半小于6点的
         // 算法 下班时间-上班时间-1.5(午餐时间)=实际上班时长
         //(2) 处理6点以后下班的，判断上班时间早于9点01，下班时间大于6点的
         // 算法 下班时间-上班时间-2(午餐时间+晚餐时间)=实际上班时长
         //(3) 存在迟到或者早退的，这种后续处理
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
    public SummaryVO summary(String staffCode) {
        SummaryVO vo =new SummaryVO();
        //查询当前员工的工时记录
        List list = workTimeRecordRepository.findByStaffCode(staffCode);
        long expectWTL =list.size()*8*60;
        double actWTL = this.caculateAcutalLength(list);
        long alvTWL = new Double(actWTL).longValue() - expectWTL;
        //设置只保留两位小数点
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        vo.setExpectWTL(decimalFormat.format(expectWTL/60)+" 小时");
        vo.setActWTL(decimalFormat.format(actWTL/60)+" 小时");
        if(alvTWL>0){
            vo.setAlvTWL(alvTWL+" 分钟");
        }else
        {
            vo.setShouldApendTWL(Math.abs(alvTWL)+"分钟");
        }
        return vo;
    }

    /***
     *
     * 将上传的文件存储到文件服务器，然后将文件存储的服务器路径返回
     * **/
    @Autowired
    private StorageProperties properties;

    Path rootLocation ;
    @Autowired
    public WorkTimeRecordService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }


    public String uploadFileToDisk(MultipartFile file) {
        String filename=file.getOriginalFilename();
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            Files.copy(file.getInputStream(),  rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return filename;
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }
}
