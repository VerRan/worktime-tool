package com.chinasoft.wt.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
@Entity
public class WorkTemplate implements java.io.Serializable {


    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模版名称
     */
    private String templateName;

    /**
     *上班时间
     */
    private String startTime;

    /**
     *下班时间
     */
    private String endTime;

    /**
     *午饭开始时间
     */
    private String lunchStartTime;

    /**
     *无法结束时间
     */
    private String lunchEndTime;

    /**
     * 晚饭开始时间
     */
    private String dinnerStartTime;

    /**
     *晚饭结束时间
     */
    private String dinnerEndTime;

    /**
     * 工作时长
     */
    private String workTimeLength;

    /**
     * 模版状态
     */
    private String sts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLunchStartTime() {
        return lunchStartTime;
    }

    public void setLunchStartTime(String lunchStartTime) {
        this.lunchStartTime = lunchStartTime;
    }

    public String getLunchEndTime() {
        return lunchEndTime;
    }

    public void setLunchEndTime(String lunchEndTime) {
        this.lunchEndTime = lunchEndTime;
    }

    public String getDinnerStartTime() {
        return dinnerStartTime;
    }

    public void setDinnerStartTime(String dinnerStartTime) {
        this.dinnerStartTime = dinnerStartTime;
    }

    public String getDinnerEndTime() {
        return dinnerEndTime;
    }

    public void setDinnerEndTime(String dinnerEndTime) {
        this.dinnerEndTime = dinnerEndTime;
    }

    public String getWorkTimeLength() {
        return workTimeLength;
    }

    public void setWorkTimeLength(String workTimeLength) {
        this.workTimeLength = workTimeLength;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     *
     */
    //@DateTimeFormat(pattern = "yyyy-mm-dd hh:mi:ss")
    private Date createTime;


}
