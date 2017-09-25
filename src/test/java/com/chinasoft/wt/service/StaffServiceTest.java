package com.chinasoft.wt.service;

import com.chinasoft.wt.model.Staff;
import com.chinasoft.wt.service.StaffService;
import com.chinasoft.wt.vo.ReturnVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by VerRan.Liu on 2017/9/22.
 *
 *
 * 测试用例可以看作是用于使用的UI界面，测试用例是
 * 各种用户交互界面的抽象 无论是APP，web，桌面程序
 * 或者是 shell交互页面
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StaffServiceTest.class)
@ComponentScan(basePackages = "com.chinasoft")
public class StaffServiceTest {
    @Autowired //如果要直接测试staffService 需要引入MockBean
    private StaffService staffService;
    @Test
    public void testAddStaff(){
        Staff staff =new Staff();
        staff.setStaffCode("0001");
        staff.setStaffName("刘恒涛");
        staffService.add(staff);
        Assert.assertEquals(1,staffService.findAll().size());
    }

    @Test
    public void testStaffLogin(){
        Staff staff =new Staff();
        staff.setStaffCode("0001");
        ReturnVO vo =staffService.login(staff) ;
        Assert.assertEquals("1",vo.getCode());;
    }
}
