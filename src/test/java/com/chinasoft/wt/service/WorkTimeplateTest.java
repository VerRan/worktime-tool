package com.chinasoft.wt.service;

import com.chinasoft.wt.model.WorkTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkTimeplateTest.class)
@ComponentScan("com.chinasoft")
public class WorkTimeplateTest {
    @Autowired
    private WorkTemplateService workTemplateService;

    @Test
    public void  addTemplate(){
        WorkTemplate workTemplat=new WorkTemplate();
        workTemplat.setCreateTime(new Date());
        workTemplat.setSts("A");
        workTemplat.setStartTime("8:00");
        workTemplat.setEndTime("17:30");
        workTemplat.setLunchStartTime("12:00");
        workTemplat.setLunchEndTime("13:30");
        workTemplat.setDinnerStartTime("17:30");
        workTemplat.setDinnerEndTime("18:00");
        workTemplat.setTemplateName("弹性工作工时模版");
        workTemplat.setWorkTimeLength("9小时");
        workTemplateService.add(workTemplat);
        Assert.assertEquals(1,workTemplateService.findAll().size());
    }


}
