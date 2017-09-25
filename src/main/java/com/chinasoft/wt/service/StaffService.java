package com.chinasoft.wt.service;

import com.chinasoft.wt.model.Staff;
import com.chinasoft.wt.repository.StaffRepository;
import com.chinasoft.wt.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
@Service
public class StaffService {
    private ReturnVO returnVO =new ReturnVO();
    @Autowired
    private StaffRepository staffRepository;
    public void add(Staff staff) {
        staffRepository.save(staff);
    }

    public java.util.List<Staff> findAll() {
        return staffRepository.findAll();
    }

    public ReturnVO login(Staff staff) {
          staff = staffRepository.findByStaffCode(staff.getStaffCode());
          if(staff==null||StringUtils.isEmpty(staff.getStaffName())){
              returnVO.setCode("201");
              returnVO.setMessage("查询员工失败");
              return returnVO;
          }
        returnVO.setCode("1");
        returnVO.setMessage("成功");
        return returnVO;
    }
}
