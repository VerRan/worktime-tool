package com.chinasoft.wt.models;

import com.chinasoft.wt.model.Staff;
import com.chinasoft.wt.repository.StaffRepository;
import com.chinasoft.wt.service.StaffService;
import com.chinasoft.wt.vo.ReturnVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class StaffModelTest {
    @Autowired
    private StaffRepository staffRepository;
    @Test
    public void testAddStaff(){
        Staff staff =new Staff();
        staff.setStaffCode("0001");
        staff.setStaffName("刘恒涛");
        staffRepository.save(staff);
        Assert.assertEquals(1,staffRepository.findAll().size());
    }
//
//    @Test
//    public void testStaffLogin(){
//        Staff staff =new Staff();
//        staff.setStaffCode("0001");
//        ReturnVO vo =staffService.login(staff) ;
//        Assert.assertEquals(1,vo.getCode());;
//    }
}
